package cn.dp.nft.limiter;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法
 */
@Component
@Data
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LeakyBucketRateLimiter implements RateLimiter {
    private final RedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "LeakyBucket:";

    /**
     * 桶的大小
     */
    @Value("")
    private Long bucketSize;
    /**
     * 漏水速率，单位:个/秒
     */
    private Long leakRate;



    /**
     * 漏水
     */
    public void leakWater() {
        // 获取包含所有路径的 Set 集合
        Set<String> pathSet = redisTemplate.opsForSet().members(KEY_PREFIX + ":pathSet");

        // 遍历所有路径，删除旧请求
        if (pathSet != null && !pathSet.isEmpty()) {
            for (String path : pathSet) {
                String redisKey = KEY_PREFIX + path;
                // 获取当前时间戳
                long now = System.currentTimeMillis();
                // 删除旧的请求, 范围是 [0, now - leakRate * 1000]
                redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, now - 1000 * leakRate);
            }
        }
    }

    @Override
    public Boolean tryAcquire(String path, int limit, int windowSize) {
        // 定义Redis锁的key，防止并发初始化问题。
        // 锁的key是由前缀和path组成，确保每个path对应一个唯一的锁。
        String lockKey = KEY_PREFIX + "LOCK:" + path;

        // 定义Redis中用于存储请求时间戳的ZSet的key，ZSet用于存储限流的计数器。
        String redisKey = KEY_PREFIX + path;

        // 定义Redis中存储所有路径的Set的key。这个Set用于跟踪所有被限流的路径。
        String pathSetKey = KEY_PREFIX + "pathSet";

        // 尝试通过Redis的setIfAbsent方法获取锁，防止多个线程同时初始化相同的资源。
        // 这里使用了一个带有100毫秒过期时间的分布式锁，确保每次只有一个线程能成功获取锁。
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 100, TimeUnit.MILLISECONDS);

        // 判断是否成功获取到锁
        if (Boolean.TRUE.equals(lockAcquired)) { // 成功获取到锁的情况下，执行限流逻辑
            try {
                // 使用Redis的ZSet（有序集合）操作对象，代表计数桶。
                ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

                // 使用Redis的Set操作对象，存储所有限流的path。
                SetOperations<String, Object> setOps = redisTemplate.opsForSet();
                setOps.add(pathSetKey, path); // 将当前路径添加到Redis的Set中

                // 获取当前系统时间的时间戳，用于记录请求时间
                long now = System.currentTimeMillis();

                // 检查当前计数桶的大小，判断是否已达到限流的上限
                Long currentSize = zSetOps.zCard(redisKey); // 获取ZSet中元素的个数（即当前请求数）
                if (currentSize == null || currentSize < bucketSize) {
                    // 如果计数桶还没有满，则可以继续处理请求
                    // 将当前请求的时间戳作为元素添加到ZSet中，时间戳既是元素也是它的score（排序依据）
                    zSetOps.add(redisKey, now, now);
                    return false; // 没有触发限流，返回false
                }

                // 如果计数桶已满，触发限流逻辑
                System.out.println("[triggerLimit] path: " + path + " bucket size: " + currentSize);
                return true; // 触发限流，返回true

            } finally {
                // 无论是否触发限流，在完成操作后需要释放锁。通过删除锁key来释放锁。
                redisTemplate.delete(lockKey);
            }
        } else {
            // 如果未能成功获取到锁，返回false表示没有进行限流操作
            return false;
        }
    }

}