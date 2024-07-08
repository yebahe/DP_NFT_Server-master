package cn.dp.nft.collection.facade;

import cn.dp.nft.api.collection.constant.CollectionEvent;

public record CollectionTrySaleRequest(String identifier, Long collectionId, Long quantity) {

    public CollectionEvent eventType() {
        return CollectionEvent.TRY_SALE;
    }
}
