package cn.dp.nft.api.order.request;

import cn.dp.nft.api.pay.constant.PayChannel;
import cn.dp.nft.api.order.constant.TradeOrderEvent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author yebahe
 */
@Getter
@Setter
public class OrderPayRequest extends BaseOrderUpdateRequest {

    /**
     * 支付方式
     */
    private PayChannel payChannel;

    /**
     * 支付流水号
     */
    private String payStreamId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.PAY;
    }
}

