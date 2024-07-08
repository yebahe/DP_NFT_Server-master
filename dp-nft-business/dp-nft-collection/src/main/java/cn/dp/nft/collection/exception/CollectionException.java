package cn.dp.nft.collection.exception;

import cn.dp.nft.base.exception.BizException;
import cn.dp.nft.base.exception.ErrorCode;

/**
 * 藏品异常
 *
 * @author yebahe
 */
public class CollectionException extends BizException {

    public CollectionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CollectionException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CollectionException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public CollectionException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public CollectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
