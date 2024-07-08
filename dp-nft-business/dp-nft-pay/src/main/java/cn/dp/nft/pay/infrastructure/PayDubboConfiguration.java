package cn.dp.nft.pay.infrastructure;

import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.order.OrderFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yebahe
 */
@Configuration
public class PayDubboConfiguration {

    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "collectionFacadeService")
    public CollectionFacadeService collectionFacadeService() {
        return collectionFacadeService;
    }

    @DubboReference(version = "1.0.0")
    private OrderFacadeService orderFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "orderFacadeService")
    public OrderFacadeService orderFacadeService() {
        return this.orderFacadeService;
    }
}
