package cn.dp.nft.ratelimiting;

import cn.dp.nft.limiter.LeakyBucketRateLimiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest // 确保Spring能够注入Bean
class LeakyBucketRateLimiterTest {
    private final RedisTemplate redisTemplate = new RedisTemplate();

    private LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(redisTemplate); // 自动注入漏桶算法类

    // 创建线程池，用于并发测试
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(30, 50, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10));

    @Test
    @DisplayName("漏桶算法测试")
    void triggerLimit() throws InterruptedException {
        // 设置桶的大小和漏水速率
        leakyBucketRateLimiter.setBucketSize(10L);
        leakyBucketRateLimiter.setLeakRate(1L);

        // 模拟多个线程同时请求限流器
        for (int i = 0; i < 8; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(20); // 控制线程同步
            for (int j = 0; j < 20; j++) {
                threadPoolExecutor.execute(() -> {
                    boolean isLimit = leakyBucketRateLimiter.tryAcquire("/test",0,0); // 调用限流方法
                    System.out.println(isLimit); // 输出是否触发限流
                    countDownLatch.countDown(); // 计数器减1
                });
            }
            countDownLatch.await(); // 等待所有线程执行完毕
            TimeUnit.SECONDS.sleep(10L); // 休眠10秒，模拟下一批请求
        }
    }
}
