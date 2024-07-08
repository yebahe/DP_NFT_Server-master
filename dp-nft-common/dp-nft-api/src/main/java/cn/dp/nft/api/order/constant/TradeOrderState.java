package cn.dp.nft.api.order.constant;

/**
 * @author yebahe
 * 订单状态
 */
public enum TradeOrderState {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单确认
     */
    CONFIRM,
    /**
     * 已付款
     */
    PAID,
    /**
     * 交易成功
     */
    FINISH,
    /**
     * 订单关闭
     */
    CLOSED;
}
