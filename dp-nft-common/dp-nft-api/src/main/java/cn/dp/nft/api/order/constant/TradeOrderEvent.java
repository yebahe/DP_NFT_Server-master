package cn.dp.nft.api.order.constant;

/**
 * @author yebahe
 */
public enum TradeOrderEvent {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单确认
     */
    CONFIRM,

    /**
     * 订单支付
     */
    PAY,

    /**
     * 订单取消
     */
    CANCEL,

    /**
     * 订单超时
     */
    TIME_OUT,

    /**
     * 订单完成
     */
    FINISH;
}
