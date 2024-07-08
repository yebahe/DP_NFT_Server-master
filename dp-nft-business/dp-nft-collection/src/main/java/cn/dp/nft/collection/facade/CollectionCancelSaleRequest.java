package cn.dp.nft.collection.facade;

import cn.dp.nft.api.collection.constant.CollectionEvent;

public record CollectionCancelSaleRequest(String identifier, Long collectionId,Long quantity) {

    public CollectionEvent eventType() {
        return CollectionEvent.CANCEL_SALE;
    }
}
