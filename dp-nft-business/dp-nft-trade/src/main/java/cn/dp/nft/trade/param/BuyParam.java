package cn.dp.nft.trade.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class BuyParam {

    @NotNull(message = "goodsId is null")
    private String goodsId;

    @NotNull(message = "goodsType is null")
    private String goodsType;

    /**
     * 商品数量
     */
    @Min(value = 1)
    private int itemCount;
}
