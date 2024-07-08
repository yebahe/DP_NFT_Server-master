package cn.dp.nft.pay.infrastructure.channel.wechat.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class WxNotifyResourceEntityAmountEntity {
    @JSONField(name = "payer_total")
    private String payerTotal;

    @JSONField(name = "total")
    private Integer total;

    @JSONField(name = "currency")
    private String currency;

    @JSONField(name = "payer_currency")
    private String payerCurrency;
}
