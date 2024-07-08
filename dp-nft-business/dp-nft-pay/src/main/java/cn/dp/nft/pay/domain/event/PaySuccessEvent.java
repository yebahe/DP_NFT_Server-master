package cn.dp.nft.pay.domain.event;

import cn.dp.nft.api.pay.constant.PayChannel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PaySuccessEvent {

    /**
     * 支付订单号
     */
    private String payOrderId;

    /**
     * 支付成功时间
     */
    private Date paySucceedTime;

    /**
     * 渠道流水号
     */
    private String channelStreamId;

    /**
     * 支付渠道
     */
    private PayChannel payChannel;

    /**
     * 支付金额
     */
    private BigDecimal paidAmount;
}
