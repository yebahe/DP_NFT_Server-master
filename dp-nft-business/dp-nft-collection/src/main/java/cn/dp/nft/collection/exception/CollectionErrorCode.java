package cn.dp.nft.collection.exception;

import cn.dp.nft.base.exception.ErrorCode;

/**
 * 藏品相关错误码
 *
 * @author yebahe
 */
public enum CollectionErrorCode implements ErrorCode {
    /**
     * 藏品相关错误
     */
    COLLECTION_SAVE_FAILED("COLLECTION_SAVE_FAILED", "藏品信息保存失败"),

    COLLECTION_UPDATE_FAILED("COLLECTION_UPDATE_FAILED", "藏品信息更新失败"),
    /**
     * 用户相关错误
     */
    COLLECTION_USER_QUERY_FAIL("COLLECTION_USER_QUERY_FAIL", "查询用户信息失败"),
    /**
     * 藏品持有相关错误
     */
    HELD_COLLECTION_SAVE_FAILED("HELD_COLLECTION_SAVE_FAILED", "藏品持有信息保存失败"),
    /**
     * 藏品相关错误
     */
    COLLECTION_QUERY_FAIL("COLLECTION_QUERY_FAIL", "查询藏品信息失败"),
    /**
     * 藏品持有相关错误
     */
    HELD_COLLECTION_QUERY_FAIL("HELD_COLLECTION_QUERY_FAIL", "查询持有藏品信息失败"),
    /**
     * 藏品库存相关错误
     */
    COLLECTION_INVENTORY_UPDATE_FAILED("COLLECTION_INVENTORY_UPDATE_FAILED", "藏品库存更新失败"),
    /**
     * 藏品流水信息保存失败
     */
    COLLECTION_STREAM_SAVE_FAILED("COLLECTION_STREAM_SAVE_FAILED", "藏品流水信息保存失败"),
    /**
     * 藏品流水信息已存在
     */
    COLLECTION_STREAM_EXIST("COLLECTION_STREAM_EXIST", "藏品流水信息已存在");


    private String code;

    private String message;

    CollectionErrorCode(String code, String message) {
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
