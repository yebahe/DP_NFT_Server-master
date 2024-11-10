package cn.dp.nft.limiter;

/**
 * 使用zset 来实现 滑动窗口
 */

import cn.dp.nft.lock.DistributeLockAntt;
import cn.dp.nft.lock.domain.Enum.LockTypeEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBitSet;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@Data
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SlidingWindowByZsetRateLimiter {
    public static final String KEY = "SlidingWindowRateLimiter:";
    private final RedisTemplate redisTemplate;  // 通过配置文件+@Bean注解 注入到IOC容器中

    // 窗口大小
    @Value("${limiter.windowSize:60}") // 使用 ${} 包裹配置项名称，并提供默认值 60
    private long windowSize; //单位是s ，一般用ms

    // 限制请求的数量
    @Value("${limiter.limitNum:100}") // 使用 ${} 包裹配置项名称，并提供默认值 100
    private long limitNum;

    /**
     * 使用滑动窗口限流
     *
     * @param path 限流路径
     * @param limitNum 允许的最大请求数
     * @param windowSize 窗口大小（秒）
     * @return true: 触发限流; false: 未触发限流
     */
    public boolean triggerLimit(String path, int limitNum, int windowSize) {
        String key = KEY + path; // 生成唯一的 Redis Key
        long currentTimeMicros = System.nanoTime() / 1000; // 当前时间戳（微秒）
        String uniqueId = UUID.randomUUID().toString(); // 生成唯一标识符

        // Lua 脚本：处理过期请求、统计请求数量、添加新请求
        String luaScript =
                "local currentTime = tonumber(ARGV[1])\n" +
                        "local member = ARGV[2]\n" +
                        "local windowSize = tonumber(ARGV[3])\n" +
                        "local limitNum = tonumber(ARGV[4])\n" +
                        "local key = KEYS[1]\n" +
                        "redis.call('ZREMRANGEBYSCORE', key, '-inf', currentTime - windowSize * 1000)\n" + // 删除过期请求
                        "local count = redis.call('ZCARD', key)\n" + // 获取当前请求数量
                        "if count < limitNum then\n" + // 判断是否超出限制
                        "    redis.call('ZADD', key, currentTime, member)\n" + // 添加新请求
                        "    return 0\n" +  // 表示未超出限制
                        "else\n" +
                        "    return 1\n" +  // 表示超出限制
                        "end";

        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

        // 执行 Lua 脚本
        Long count = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key),
                String.valueOf(currentTimeMicros), uniqueId, String.valueOf(windowSize), String.valueOf(limitNum));

        // 判断是否触发限流
        return (count != null && count >= 1); // true：触发限流
    }
}
