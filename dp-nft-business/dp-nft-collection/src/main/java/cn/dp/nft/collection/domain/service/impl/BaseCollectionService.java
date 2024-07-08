package cn.dp.nft.collection.domain.service.impl;

import cn.dp.nft.collection.exception.CollectionErrorCode;
import cn.dp.nft.collection.exception.CollectionException;
import cn.dp.nft.collection.facade.CollectionCancelSaleRequest;
import cn.dp.nft.collection.facade.CollectionConfirmSaleRequest;
import cn.dp.nft.collection.facade.CollectionTrySaleRequest;
import cn.dp.nft.collection.infrastructure.mapper.CollectionMapper;
import cn.dp.nft.collection.infrastructure.mapper.CollectionStreamMapper;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import cn.dp.nft.api.collection.request.CollectionSaleRequest;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.entity.CollectionStream;
import cn.dp.nft.collection.domain.request.HeldCollectionCreateRequest;
import cn.dp.nft.collection.domain.response.CollectionConfirmSaleResponse;
import cn.dp.nft.collection.domain.service.CollectionService;
import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class BaseCollectionService extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    @Autowired
    private HeldCollectionService heldCollectionService;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CollectionStreamMapper collectionStreamMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection create(CollectionChainRequest request) {
        Collection existCollection = collectionMapper.selectByIdentifier(request.getIdentifier(), request.getClassId());
        if (existCollection != null) {
            return existCollection;
        }

        Collection collection = Collection.create(request);
        var saveResult = this.save(collection);
        if (!saveResult) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_SAVE_FAILED);
        }

        CollectionStream stream = new CollectionStream(collection, request.getIdentifier(), request.getEventType());
        saveResult = collectionStreamMapper.insert(stream) == 1;
        Assert.isTrue(saveResult, () -> new CollectionException(CollectionErrorCode.COLLECTION_STREAM_SAVE_FAILED));

        return collection;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean trySale(CollectionTrySaleRequest request) {
        //流水校验
        CollectionStream existStream = collectionStreamMapper.selectByIdentifier(request.identifier(), request.eventType().name(), request.collectionId());
        if (null != existStream) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_STREAM_EXIST);
        }

        //核心逻辑执行
        int result = collectionMapper.trySale(request.collectionId(), request.quantity());
        Assert.isTrue(result == 1, () -> new CollectionException(CollectionErrorCode.COLLECTION_SAVE_FAILED));

        //查询出最新的值
        Collection collection = this.getById(request.collectionId());

        //新增collection流水
        CollectionStream stream = new CollectionStream(collection, request.identifier(), request.eventType());
        result = collectionStreamMapper.insert(stream);
        Assert.isTrue(result > 0, () -> new CollectionException(CollectionErrorCode.COLLECTION_STREAM_SAVE_FAILED));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean cancelSale(CollectionCancelSaleRequest request) {
        //流水校验
        CollectionStream existStream = collectionStreamMapper.selectByIdentifier(request.identifier(), request.eventType().name(), request.collectionId());
        if (null != existStream) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_STREAM_EXIST);
        }

        //核心逻辑执行
        int result = collectionMapper.cancelSale(request.collectionId(), request.quantity());
        Assert.isTrue(result == 1, () -> new CollectionException(CollectionErrorCode.COLLECTION_SAVE_FAILED));

        //查询出最新的值
        Collection collection = this.getById(request.collectionId());

        //新增collection流水
        CollectionStream stream = new CollectionStream(collection, request.identifier(), request.eventType());
        result = collectionStreamMapper.insert(stream);
        Assert.isTrue(result > 0, () -> new CollectionException(CollectionErrorCode.COLLECTION_STREAM_SAVE_FAILED));
        return true;
    }

    protected Boolean doExecute(CollectionSaleRequest request, Consumer<CollectionSaleRequest> consumer) {
        //流水校验
        CollectionStream existStream = collectionStreamMapper.selectByIdentifier(request.getIdentifier(), request.getEventType().name(), request.getCollectionId());
        if (null != existStream) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_STREAM_EXIST);
        }

        //核心逻辑执行
        consumer.accept(request);

        //查询出最新的值
        Collection collection = this.getById(request.getCollectionId());

        //新增collection流水
        CollectionStream stream = new CollectionStream(collection, request.getIdentifier(), request.getEventType());
        int result = collectionStreamMapper.insert(stream);
        Assert.isTrue(result > 0, () -> new CollectionException(CollectionErrorCode.COLLECTION_STREAM_SAVE_FAILED));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CollectionConfirmSaleResponse confirmSale(CollectionConfirmSaleRequest request) {

        //流水校验
        CollectionStream existStream = collectionStreamMapper.selectByIdentifier(request.identifier(), request.eventType().name(), request.collectionId());
        if (null != existStream) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_STREAM_EXIST);
        }
        Collection collection = this.getById(request.collectionId());

        int result = collectionMapper.confirmSale(request.collectionId(), collection.getOccupiedInventory(), request.quantity());
        Assert.isTrue(result == 1, () -> new CollectionException(CollectionErrorCode.COLLECTION_SAVE_FAILED));

        //新增collection流水
        CollectionStream stream = new CollectionStream(collection, request.identifier(), request.eventType());
        stream.setOccupiedInventory(collection.getOccupiedInventory() + request.quantity());

        result = collectionStreamMapper.insert(stream);
        Assert.isTrue(result > 0, () -> new CollectionException(CollectionErrorCode.COLLECTION_STREAM_SAVE_FAILED));

        HeldCollectionCreateRequest heldCollectionCreateRequest = new HeldCollectionCreateRequest(request,String.valueOf(collection.getOccupiedInventory() + 1));
        var heldCollection = heldCollectionService.create(heldCollectionCreateRequest);

        CollectionConfirmSaleResponse collectionSaleResponse = new CollectionConfirmSaleResponse();
        collectionSaleResponse.setSuccess(true);
        collectionSaleResponse.setCollection(collection);
        collectionSaleResponse.setHeldCollection(heldCollection);
        return collectionSaleResponse;
    }

    @Override
    @Cached(name = ":collection:cache:id:", expire = 60,localExpire = 10, timeUnit = TimeUnit.MINUTES, cacheType = CacheType.BOTH, key = "#collectionId", cacheNullValue = true)
    @CacheRefresh(refresh = 50, timeUnit = TimeUnit.MINUTES)
    public Collection queryById(Long collectionId) {
        return getById(collectionId);
    }
}
