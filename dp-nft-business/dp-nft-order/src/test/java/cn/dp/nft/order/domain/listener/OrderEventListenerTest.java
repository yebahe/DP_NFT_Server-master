package cn.dp.nft.order.domain.listener;

import cn.dp.nft.order.domain.OrderBaseTest;
import cn.dp.nft.order.domain.entity.TradeOrder;
import cn.dp.nft.order.domain.listener.event.OrderCreateEvent;
import cn.dp.nft.order.domain.service.OrderManageService;
import cn.dp.nft.order.domain.service.OrderReadService;
import cn.dp.nft.api.collection.response.CollectionSaleResponse;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.api.order.request.OrderCreateRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author yebahe
 */
public class OrderEventListenerTest extends OrderBaseTest {

    @Autowired
    OrderEventListener orderEventListener;

    @Autowired
    OrderManageService orderManageService;
    @Autowired
    OrderReadService orderReadService;

    @MockBean
    public CollectionFacadeService collectionFacadeService;

    @Test
    public void testOnApplicationEvent() {
        CollectionSaleResponse response = new CollectionSaleResponse();
        response.setSuccess(true);
        when(collectionFacadeService.trySale(any())).thenReturn(response);

        OrderCreateRequest orderCreateRequest = orderCreateRequest();

        String orderId = orderManageService.create(orderCreateRequest).getOrderId();

        TradeOrder tradeOrder = orderReadService.getOrder(orderId);

        orderEventListener.onApplicationEvent(new OrderCreateEvent(tradeOrder));

        try {
            //等子线程执行完
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        tradeOrder = orderReadService.getOrder(orderId);

        Assert.assertEquals(tradeOrder.getOrderState(), TradeOrderState.CONFIRM);
        Assert.assertNotNull(tradeOrder.getOrderConfirmedTime());
    }

}