package cn.dp.nft.limiter;

import com.alibaba.nacos.shaded.io.grpc.internal.JsonUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Data
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TokenBucketRateLimiter {
//    public static final String KEY = "SlidingWindowRateLimiter:";
    private final RedisTemplate redisTemplate;  // 通过配置文件+@Bean注解 注入到IOC容器中
//
//    // 窗口大小
//    @Value("${limiter.tokenBucketRate:6}") // 使用 ${} 包裹配置项名称，并提供默认值 60
//    private long tokenBucketRate; //单位是s ，表示每秒中生成的token的个数
//
//    // 限制请求的数量 桶的大小
//    @Value("${limiter.bucketSize:100}") // 使用 ${} 包裹配置项名称，并提供默认值 100
//    private long bucketSize;
//
//    /**
//     * 限流算法
//     * 系统以固定的速率生产令牌、请求拿到令牌才可以去执行、如果桶空了说明触发限流了
//     */
//    public boolean triggerLimit(String path){
//        //
//return false;
//    }
    // 1、contrller addController
    // 2、service


}
