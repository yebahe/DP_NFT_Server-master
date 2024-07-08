package cn.dp.nft.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author yebahe
 */
@Configuration
@EnableMethodCache(basePackages = "cn.hollis.nft.turbo")
public class CacheConfiguration {
}
