package cn.dp.nft.limiter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 固定滑动窗口 ：设定每个窗口的
 */
@Component
@Data
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FixedWindowRateLimiter {

    public static final String KEY = "fixedWindowRateLimiter:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final Redisson redissonClient;

    @Value("${limiter.windowSize:60}") // 窗口大小，默认 60 秒
    private long windowSize;

    @Value("${limiter.limitNum:100}") // 最大请求数，默认 100 次
    private int limitNum;

    /**
     * 使用 Redis + Lua 实现限流
     *
     * @param path 要限流的路径
     * @return true 表示触发限流，false 表示未触发限流
     */
    public boolean triggerLimit(String path) {
        String redisKey = KEY + path;

        // Lua 脚本：计数器递增并在第一次请求时设置过期时间
        String luaScript = "local current = redis.call('INCR', KEYS[1])\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('EXPIRE', KEYS[1], ARGV[1])\n" +
                "end\n" +
                "return current";

        RedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        Long currentCount = redisTemplate.execute(script, Collections.singletonList(redisKey), windowSize);

        // 检查是否超过限制
        return currentCount != null && currentCount > limitNum;
    }

    public boolean triggerLimitByRedisKey(String redisKey) {
        redisKey = KEY + redisKey; // 针对不同路径进行限流
        // 使用 RedisTemplate 实现计数
        Long count = redisTemplate.opsForValue().increment(redisKey);
        // 如果为1的话，说明窗口刚初始化，设置过期时间
        if (count != null && count == 1) {
            // 设置过期时间，作为窗口的起始
            redisTemplate.expire(redisKey, windowSize, TimeUnit.SECONDS);
        }
        // 触发限流，超过限制则返回 true
        if (count != null && count > limitNum) {
            // 超过限制后，减回去（不计超出的请求）
            redisTemplate.opsForValue().decrement(redisKey);
            return true; // 触发限流
        }
        return false; // 未触发限流
    }
}


