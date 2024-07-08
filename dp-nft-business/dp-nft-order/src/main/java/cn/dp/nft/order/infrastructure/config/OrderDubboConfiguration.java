package cn.dp.nft.order.infrastructure.config;

import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.goods.service.GoodsFacadeService;
import cn.dp.nft.api.pay.service.PayFacadeService;
import cn.dp.nft.api.user.service.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yebahe
 */
@Configuration
public class OrderDubboConfiguration {

    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private PayFacadeService payFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "collectionFacadeService")
    public CollectionFacadeService collectionFacadeService() {
        return this.collectionFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userFacadeService")
    public UserFacadeService userFacadeService() {
        return this.userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "payFacadeService")
    public PayFacadeService payFacadeService() {
        return this.payFacadeService;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "goodsFacadeService")
    public GoodsFacadeService goodsFacadeService() {
        return this.goodsFacadeService;
    }
}
