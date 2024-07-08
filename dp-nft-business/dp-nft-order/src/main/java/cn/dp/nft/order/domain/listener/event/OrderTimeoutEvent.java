package cn.dp.nft.order.domain.listener.event;

import cn.dp.nft.api.order.request.BaseOrderRequest;
import org.springframework.context.ApplicationEvent;

/**
 * @author yebahe
 */
public class OrderTimeoutEvent extends ApplicationEvent {

    public OrderTimeoutEvent(BaseOrderRequest baseOrderRequest) {
        super(baseOrderRequest);
    }
}
