package cn.dp.nft.lock;

import cn.dp.nft.lock.DistributeLockAntt;
import cn.dp.nft.lock.domain.Enum.LockTypeEnum;
import cn.dp.nft.lock.infrastructure.DistributionLockDefinitionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class DistributeLockAnttAspect {

    @Autowired
    private static StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "lock:";
    private static final String LOCK_VALUE_SUFFIX = ":value";
    private static final String LOCK_COUNT_SUFFIX = ":count";
    private static final String RELEASE_SCRIPT =
            "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
                    "   return redis.call('DEL', KEYS[1]) " +
                    "else " +
                    "   return 0 " +
                    "end";

    // 扫描的任务队列
    private static ConcurrentLinkedQueue<DistributionLockDefinitionHolder> holderList = new ConcurrentLinkedQueue<>();

    // 线程池，维护keyAliveTime
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("redisLock-schedule-pool").daemon(true).build());

    static {
        // 两秒执行一次「续时」操作
        SCHEDULER.scheduleAtFixedRate(() -> {
            // 这里记得加 try-catch，否者报错后定时任务将不会再执行=-=
            Iterator<DistributionLockDefinitionHolder> iterator = holderList.iterator();
            while (iterator.hasNext()) {
                DistributionLockDefinitionHolder holder = iterator.next();
                // 判空
                if (holder == null) {
                    iterator.remove();
                    continue;
                }
                // 判断 key 是否还有效，无效的话进行移除
                if (redisTemplate.opsForValue().get(holder.getKey()) == null) {
                    iterator.remove();
                    continue;
                }
                // 超时重试次数，超过时给线程设定中断
                if (holder.getCurrentCount() > holder.getTryCount()) {
                    holder.getCurrentTread().interrupt();
                    iterator.remove();
                    continue;
                }
                // 判断是否进入最后三分之一时间
                long curTime = System.currentTimeMillis();
                boolean shouldExtend = (holder.getLastModifyTime() + holder.getModifyPeriod()) <= curTime;
                if (shouldExtend) {
                    holder.setLastModifyTime(curTime);
                    redisTemplate.expire(holder.getKey(), holder.getLockTime(), TimeUnit.SECONDS);
                    redisTemplate.expire(holder.getReentrantKey(), holder.getLockTime(), TimeUnit.SECONDS);
                    log.info("businessKey : [" + holder.getKey() + "], try count : " + holder.getCurrentCount());
                    holder.setCurrentCount(holder.getCurrentCount() + 1);
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    // 使用ThreadLocal存储唯一标识符
    private ThreadLocal<String> uniqueThreadId = ThreadLocal.withInitial(() -> String.valueOf(Thread.currentThread().getId()));

    @Around("@annotation(cn.dp.nft.lock.DistributeLockAntt)")
    public Object lockAndExecute(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistributeLockAntt distributeLock = method.getAnnotation(DistributeLockAntt.class);
        LockTypeEnum typeEnum = distributeLock.typeEnum();
        String ukString = distributeLock.key();
        String businessUniqueKey = typeEnum.getUniqueKey(ukString);
        String lockKey = businessUniqueKey + LOCK_VALUE_SUFFIX;
        String lockReentrantCountKey = businessUniqueKey + LOCK_COUNT_SUFFIX;
        String threadId = uniqueThreadId.get();
        long lockTime = distributeLock.lockTime();
        TimeUnit unit = distributeLock.unit();

        boolean isLocked = false;
        int retryCount = 0;

        try {
            // 尝试获取锁
            do {
                Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(lockKey, threadId, lockTime, unit);
                if (Boolean.TRUE.equals(isSuccess)) {
                    redisTemplate.opsForValue().set(lockReentrantCountKey, "1", lockTime, unit);
                    isLocked = true;
                    break;
                } else {
                    String currentValue = redisTemplate.opsForValue().get(lockKey);
                    if (threadId.equals(currentValue)) {
                        redisTemplate.opsForValue().increment(lockReentrantCountKey);
                        isLocked = true;
                        break;
                    }
                }
                retryCount++;
                if (distributeLock.maxRetryCount() != -1 && retryCount >= distributeLock.maxRetryCount()) {
                    break;
                }
                if (distributeLock.waitTime() > 0) {
                    Thread.sleep(distributeLock.waitTime());
                }
            } while (distributeLock.maxRetryCount() == -1 || retryCount < distributeLock.maxRetryCount());

            if (!isLocked) {
                throw new Exception("Unable to acquire lock after max retries");
            }

            // 加入延时队列
            holderList.add(new DistributionLockDefinitionHolder(lockKey, lockReentrantCountKey, lockTime, System.currentTimeMillis(),
                    Thread.currentThread(), distributeLock.maxRetryCount()));

            // 执行业务操作
            Object result = pjp.proceed();

            // 检查线程中断状态
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("You had been interrupted =-=");
            }

            return result;

        } catch (InterruptedException e) {
            log.error("Interrupt exception, rollback transaction", e);
            throw new Exception("Interrupt exception, please send request again");
        } catch (Exception e) {
            log.error("has some error, please check again", e);
            throw e;
        } finally {
            // 请求结束后，减少计数器或释放锁
            RedisScript<Long> releaseScript = new DefaultRedisScript<>(RELEASE_SCRIPT, Long.class);
            Long result = redisTemplate.execute(releaseScript, Collections.singletonList(lockKey), threadId);
            if (result != null && result == 1) {
                redisTemplate.delete(lockKey);
                redisTemplate.delete(lockReentrantCountKey);
            }
            log.info("release the lock, businessKey is [{}]", lockKey);
        }
    }
}

