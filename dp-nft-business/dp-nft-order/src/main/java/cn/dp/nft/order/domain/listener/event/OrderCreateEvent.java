package cn.dp.nft.order.domain.listener.event;

import cn.dp.nft.order.domain.entity.TradeOrder;
import org.springframework.context.ApplicationEvent;

/**
 * @author yebahe
 */
public class OrderCreateEvent extends ApplicationEvent {

    public OrderCreateEvent(TradeOrder tradeOrder) {
        super(tradeOrder);
    }
}
