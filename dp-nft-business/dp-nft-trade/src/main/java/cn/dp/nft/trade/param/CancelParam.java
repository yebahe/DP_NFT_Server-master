package cn.dp.nft.trade.param;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class CancelParam {

    @NotNull(message = "orderId is null")
    private String orderId;
}
