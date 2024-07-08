package cn.dp.nft.trade.exception;

import cn.dp.nft.base.exception.ErrorCode;

/**
 * @author yebahe
 */
public enum TradeErrorCode implements ErrorCode {

    /**
     * 订单创建失败
     */
    ORDER_CREATE_FAILED("ORDER_CREATE_FAILED", "订单创建失败"),
    /**
     * 无支付权限
     */
    PAY_PERMISSION_DENIED("PAY_PERMISSION_DENIED", "无支付权限"),
    /**
     * 支付创建失败
     */
    PAY_CREATE_FAILED("PAY_CREATE_FAILED", "支付创建失败"),

    /**
     * 商品不可售卖
     */
    GOODS_NOT_FOR_SALE("GOODS_NOT_FOR_SALE", "商品不可售卖"),

    /**
     * 商品不存在
     */
    GOODS_NOT_EXIST("GOODS_NOT_EXIST", "商品不存在"),

    /**
     * 订单不可支付
     */
    ORDER_IS_CANNOT_PAY("ORDER_IS_CANNOT_PAY", "订单不可支付"),
    /**
     * 订单取消失败
     */
    ORDER_CANCEL_FAILED("ORDER_CANCEL_FAILED", "订单取消失败");

    private String code;

    private String message;

    TradeErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
