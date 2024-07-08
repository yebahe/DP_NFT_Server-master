package cn.dp.nft.order.listener;

import cn.dp.nft.api.order.constant.TradeOrderEvent;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.api.order.request.BaseOrderUpdateRequest;
import cn.dp.nft.api.order.request.OrderCancelRequest;
import cn.dp.nft.api.order.request.OrderTimeoutRequest;
import cn.dp.nft.api.order.response.OrderResponse;
import cn.dp.nft.order.domain.entity.TradeOrder;
import cn.dp.nft.order.domain.service.OrderManageService;
import cn.dp.nft.order.domain.service.OrderReadService;
import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yebahe
 */
@Component
public class OrderCloseTransactionListener implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderCloseTransactionListener.class);

    @Autowired
    private OrderManageService orderManageService;

    @Autowired
    private OrderReadService orderReadService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            Map<String, String> headers = message.getProperties();
            String closeType = headers.get("CLOSE_TYPE");

            OrderResponse response = null;
            if (TradeOrderEvent.CANCEL.name().equals(closeType)) {
                OrderCancelRequest cancelRequest = JSON.parseObject(JSON.parseObject(message.getBody()).getString("body"), OrderCancelRequest.class);
                logger.info("executeLocalTransaction , baseOrderUpdateRequest = {} , closeType = {}", JSON.toJSONString(cancelRequest), closeType);
                response = orderManageService.cancel(cancelRequest);
            } else if (TradeOrderEvent.TIME_OUT.name().equals(closeType)) {
                OrderTimeoutRequest timeoutRequest = JSON.parseObject(JSON.parseObject(message.getBody()).getString("body"), OrderTimeoutRequest.class);
                logger.info("executeLocalTransaction , baseOrderUpdateRequest = {} , closeType = {}", JSON.toJSONString(timeoutRequest), closeType);
                response = orderManageService.timeout(timeoutRequest);
            } else {
                throw new UnsupportedOperationException("unsupported closeType " + closeType);
            }

            if (response.getSuccess()) {
                return LocalTransactionState.COMMIT_MESSAGE;
            } else {
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        } catch (Exception e) {
            logger.error("executeLocalTransaction error, message = {}", message, e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        BaseOrderUpdateRequest baseOrderUpdateRequest = JSON.parseObject(messageExt.getBody(), BaseOrderUpdateRequest.class);

        TradeOrder tradeOrder = orderReadService.getOrder(baseOrderUpdateRequest.getOrderId());

        if (tradeOrder.getOrderState() == TradeOrderState.CLOSED) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
