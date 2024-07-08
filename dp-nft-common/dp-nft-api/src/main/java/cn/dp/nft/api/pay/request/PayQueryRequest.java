package cn.dp.nft.api.pay.request;

import cn.dp.nft.api.pay.constant.PayOrderState;
import cn.dp.nft.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PayQueryRequest extends BaseRequest {

    @NotNull(message = "payQueryCondition is null")
    private PayQueryCondition payQueryCondition;

    private PayOrderState payOrderState;

    @NotNull(message = "payerId is null")
    private String payerId;

}
