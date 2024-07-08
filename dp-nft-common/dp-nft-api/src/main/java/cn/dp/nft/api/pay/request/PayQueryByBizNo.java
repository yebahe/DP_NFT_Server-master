package cn.dp.nft.api.pay.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PayQueryByBizNo implements PayQueryCondition {

    private String bizNo;

    private String bizType;
}
