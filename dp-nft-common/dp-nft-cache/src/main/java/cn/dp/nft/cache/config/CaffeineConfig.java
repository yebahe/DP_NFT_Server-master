package cn.dp.nft.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000) // 设置最大缓存条目数
                .expireAfterWrite(10, TimeUnit.MINUTES) // 设置写入后10分钟过期
                .build();
    }
}
