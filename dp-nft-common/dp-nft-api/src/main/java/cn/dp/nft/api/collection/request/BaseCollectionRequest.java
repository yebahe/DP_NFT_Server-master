package cn.dp.nft.api.collection.request;

import cn.dp.nft.api.collection.constant.CollectionEvent;
import cn.dp.nft.base.request.BaseRequest;

/**
 * @author yebahe
 */
public abstract class BaseCollectionRequest extends BaseRequest {

    /**
     * 获取事件类型
     * @return
     */
    public abstract CollectionEvent getEventType();
}
