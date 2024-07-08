package cn.dp.nft.order.domain.listener;

import cn.dp.nft.api.order.OrderFacadeService;
import cn.dp.nft.api.order.request.OrderConfirmRequest;
import cn.dp.nft.api.user.constant.UserType;
import cn.dp.nft.order.domain.entity.TradeOrder;
import cn.dp.nft.order.domain.listener.event.OrderCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yebahe
 */
@Component
public class OrderEventListener {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @EventListener(OrderCreateEvent.class)
    @Async("orderListenExecutor")
    public void onApplicationEvent(OrderCreateEvent event) {

        TradeOrder tradeOrder = (TradeOrder) event.getSource();
        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setOperator(UserType.PLATFORM.name());
        confirmRequest.setOperatorType(UserType.PLATFORM);
        confirmRequest.setOrderId(tradeOrder.getOrderId());
        confirmRequest.setIdentifier(tradeOrder.getIdentifier());
        confirmRequest.setOperateTime(new Date());

        orderFacadeService.confirm(confirmRequest);
    }
}
