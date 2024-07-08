package cn.dp.nft.es.config;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * ES配置
 *
 * @author yebahe
 */
@Configuration
@EsMapperScan("cn.dp.nft.*.infrastructure.es.mapper")
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
public class EsConfiguration {

}
