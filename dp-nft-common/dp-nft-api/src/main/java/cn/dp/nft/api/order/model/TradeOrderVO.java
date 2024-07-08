package cn.dp.nft.api.order.model;

import cn.dp.nft.api.goods.constant.GoodsType;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.api.pay.constant.PayChannel;
import cn.dp.nft.api.user.constant.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yebahe
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TradeOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 买家id
     */
    private String buyerId;

    /**
     * 买家名称
     */
    private String buyerName;

    /**
     * 买家id类型
     */
    private UserType buyerType;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 卖家名称
     */
    private String sellerName;

    /**
     * 卖家id类型
     */
    private UserType sellerType;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 商品数量
     */
    private Integer itemCount;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 支付成功时间
     */
    private Date paySucceedTime;

    /**
     * 下单确认时间
     */
    private Date orderConfirmedTime;

    /**
     * 订单关闭时间
     */
    private Date orderClosedTime;

    /**
     * 商品Id
     */
    private String goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 图片地址
     */
    private String goodsPicUrl;

    /**
     * 支付方式
     */
    private PayChannel payChannel;

    /**
     * 支付流水号
     */
    private String payStreamId;

    /**
     * 订单状态
     */
    private TradeOrderState orderState;

    /**
     * 是否超时
     */
    private Boolean timeout;

    /**
     * 支付超时时间
     */
    private Date payExpireTime;
}
