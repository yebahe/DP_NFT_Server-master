package cn.dp.nft.api.order.request;

import cn.dp.nft.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class OrderPageQueryRequest extends PageRequest {

    /**
     * 买家id
     */
    private String buyerId;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单状态
     */
    private String state;
}
