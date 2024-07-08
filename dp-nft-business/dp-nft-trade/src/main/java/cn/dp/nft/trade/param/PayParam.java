package cn.dp.nft.trade.param;

import cn.dp.nft.api.pay.constant.PayChannel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PayParam {

    @NotNull(message = "orderId is null")
    private String orderId;

    @NotNull(message = "payChannel is null")
    private PayChannel payChannel;

}
