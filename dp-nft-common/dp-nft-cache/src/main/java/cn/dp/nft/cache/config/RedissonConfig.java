package cn.dp.nft.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.redisson.config}")
    private String redissonConfig;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(redissonConfig);
        return Redisson.create(config);
    }
}
