package cn.dp.nft.collection.facade;

import cn.dp.nft.api.collection.request.*;
import cn.dp.nft.collection.domain.response.CollectionInventoryResponse;
import cn.dp.nft.collection.domain.service.CollectionService;
import cn.dp.nft.collection.domain.service.impl.HeldCollectionService;
import cn.dp.nft.collection.exception.CollectionErrorCode;
import cn.dp.nft.collection.exception.CollectionException;
import cn.dp.nft.api.chain.constant.ChainOperateBizTypeEnum;
import cn.dp.nft.api.chain.request.ChainProcessRequest;
import cn.dp.nft.api.chain.service.ChainFacadeService;
import cn.dp.nft.api.collection.model.CollectionInventoryVO;
import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.api.collection.model.HeldCollectionVO;
import cn.dp.nft.api.collection.response.CollectionChainResponse;
import cn.dp.nft.api.collection.response.CollectionDestroyResponse;
import cn.dp.nft.api.collection.response.CollectionSaleResponse;
import cn.dp.nft.api.collection.response.CollectionTransferResponse;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.user.request.UserQueryRequest;
import cn.dp.nft.api.user.response.UserQueryResponse;
import cn.dp.nft.api.user.response.data.UserInfo;
import cn.dp.nft.api.user.service.UserFacadeService;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.base.response.SingleResponse;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.entity.HeldCollection;
import cn.dp.nft.collection.domain.entity.convertor.CollectionConvertor;
import cn.dp.nft.collection.domain.entity.convertor.HeldCollectionConvertor;
import cn.dp.nft.collection.domain.request.CollectionInventoryRequest;
import cn.dp.nft.collection.domain.request.HeldCollectionDestroyRequest;
import cn.dp.nft.collection.domain.request.HeldCollectionTransferRequest;
import cn.dp.nft.collection.domain.response.CollectionConfirmSaleResponse;
import cn.dp.nft.collection.domain.service.impl.redis.CollectionInventoryRedisService;
import cn.dp.nft.rpc.facade.Facade;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 藏品服务
 *
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class CollectionFacadeServiceImpl implements CollectionFacadeService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionFacadeServiceImpl.class);

    @Autowired
    private ChainFacadeService chainFacadeService;

    @Autowired
    private UserFacadeService userFacadeService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private HeldCollectionService heldCollectionService;

    @Autowired
    private CollectionInventoryRedisService collectionInventoryRedisService;

    @Autowired
    private   Cache<Long, CollectionVO> caffeineCache;

    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;



    @Override
    @Facade
    public CollectionChainResponse chain(CollectionChainRequest request) {
        Collection collection = collectionService.create(request);
        ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
        chainProcessRequest.setIdentifier(request.getIdentifier());
        chainProcessRequest.setClassId(request.getClassId());
        chainProcessRequest.setClassName(request.getName());
        chainProcessRequest.setBizType(ChainOperateBizTypeEnum.COLLECTION.name());
        chainProcessRequest.setBizId(collection.getId().toString());
        var chainRes = chainFacadeService.chain(chainProcessRequest);
        CollectionChainResponse response = new CollectionChainResponse();
        response.setSuccess(chainRes.getSuccess());
        if (chainRes.getSuccess()) {
            response.setCollectionId(collection.getId());
        }
        return response;
    }

    @Override
    @Facade
    public CollectionSaleResponse confirmSale(CollectionSaleRequest request) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserId(Long.valueOf(request.getUserId()));
        CompletableFuture<UserQueryResponse<UserInfo>> queryUserFuture = CompletableFuture.supplyAsync(() -> userFacadeService.query(userQueryRequest));

        CollectionConfirmSaleRequest confirmSaleRequest = new CollectionConfirmSaleRequest(request.getIdentifier(), request.getCollectionId(), request.getQuantity(),
                request.getBizNo(), request.getBizType(),request.getUserId(),request.getName(),request.getCover(),request.getPurchasePrice());
        CollectionConfirmSaleResponse confirmSaleResponse = collectionService.confirmSale(confirmSaleRequest);
        CollectionSaleResponse response = new CollectionSaleResponse();

        if (confirmSaleResponse.getSuccess()) {
            Collection collection = confirmSaleResponse.getCollection();
            HeldCollection heldCollection = confirmSaleResponse.getHeldCollection();

            Thread.ofVirtual().start(() -> {
                ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
                chainProcessRequest.setRecipient(getUserInfo(queryUserFuture).getBlockChainUrl());
                chainProcessRequest.setClassId(collection.getClassId());
                chainProcessRequest.setClassName(collection.getName());
                chainProcessRequest.setSerialNo(heldCollection.getSerialNo());
                chainProcessRequest.setBizId(heldCollection.getId().toString());
                chainProcessRequest.setBizType(ChainOperateBizTypeEnum.HELD_COLLECTION.name());
                chainProcessRequest.setIdentifier(confirmSaleResponse.getHeldCollection().getId().toString());
                chainFacadeService.mint(chainProcessRequest);
            });

            response.setSuccess(true);
            response.setHeldCollectionId(heldCollection.getId());
        } else {
            response.setSuccess(false);
            response.setResponseCode(confirmSaleResponse.getResponseCode());
            response.setResponseMessage(confirmSaleResponse.getResponseMessage());
        }

        return response;
    }

    @Override
    @Facade
    public CollectionSaleResponse trySale(CollectionSaleRequest request) {
        CollectionTrySaleRequest collectionTrySaleRequest = new CollectionTrySaleRequest(request.getIdentifier(),request.getCollectionId(),request.getQuantity());
        Boolean trySaleResult = collectionService.trySale(collectionTrySaleRequest);
        CollectionSaleResponse response = new CollectionSaleResponse();
        response.setSuccess(trySaleResult);
        return response;
    }

    @Override
    @Facade
    public CollectionSaleResponse cancelSale(CollectionSaleRequest request) {
        CollectionCancelSaleRequest collectionCancelSaleRequest = new CollectionCancelSaleRequest(request.getIdentifier(), request.getCollectionId(),request.getQuantity());
        Boolean cancelSaleResult = collectionService.cancelSale(collectionCancelSaleRequest);
        CollectionSaleResponse response = new CollectionSaleResponse();

        response.setSuccess(cancelSaleResult);
        return response;
    }

    private UserInfo getUserInfo(CompletableFuture<UserQueryResponse<UserInfo>> queryUserFuture) {
        UserQueryResponse<UserInfo> userQueryResponse;

        try {
            userQueryResponse = queryUserFuture.get();
            if (!userQueryResponse.getSuccess() || null == userQueryResponse.getData()) {
                throw new CollectionException(CollectionErrorCode.COLLECTION_USER_QUERY_FAIL);
            }
            return userQueryResponse.getData();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Facade
    public CollectionTransferResponse transfer(CollectionTransferRequest request) {
        UserQueryRequest buyerQuery = new UserQueryRequest();
        buyerQuery.setUserId(request.getBuyerId());
        var buyerRes = userFacadeService.query(buyerQuery);
        UserQueryRequest sellerQuery = new UserQueryRequest();
        sellerQuery.setUserId(request.getSellerId());
        var sellerRes = userFacadeService.query(sellerQuery);
        if (!buyerRes.getSuccess() || null == buyerRes.getData() || !sellerRes.getSuccess()
                || null == sellerRes.getData()) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_USER_QUERY_FAIL);
        }
        Collection collection = collectionService.getById(request.getCollectionId());
        if (null == collection) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_QUERY_FAIL);
        }
        HeldCollection heldCollection = heldCollectionService.getById(request.getHeldCollectionId());
        if (null == heldCollection || StringUtils.isNotBlank(heldCollection.getNftId())) {
            throw new CollectionException(CollectionErrorCode.HELD_COLLECTION_QUERY_FAIL);
        }
        ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
        chainProcessRequest.setRecipient(buyerRes.getData().getBlockChainUrl());
        chainProcessRequest.setOwner(sellerRes.getData().getBlockChainUrl());
        chainProcessRequest.setClassId(collection.getClassId());
        chainProcessRequest.setIdentifier(request.getIdentifier());
        chainProcessRequest.setNtfId(heldCollection.getNftId());
        var transferRes = chainFacadeService.transfer(chainProcessRequest);
        CollectionTransferResponse response = new CollectionTransferResponse();
        response.setSuccess(transferRes.getSuccess());
        if (transferRes.getSuccess()) {
            //更新藏品持有表
            HeldCollectionTransferRequest heldCollectionTransferRequest = new HeldCollectionTransferRequest();
            BeanUtils.copyProperties(request, heldCollectionTransferRequest);
            var newHeldCollection = heldCollectionService.transfer(heldCollectionTransferRequest);
            response.setHeldCollectionId(newHeldCollection.getId());
        }
        return response;
    }

    @Override
    @Facade
    public CollectionDestroyResponse destroy(CollectionDestroyRequest request) {
        Collection collection = collectionService.getById(request.getCollectionId());
        if (null == collection) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_QUERY_FAIL);
        }
        HeldCollection heldCollection = heldCollectionService.getById(request.getHeldCollectionId());
        if (null == heldCollection || StringUtils.isNotBlank(heldCollection.getNftId())) {
            throw new CollectionException(CollectionErrorCode.HELD_COLLECTION_QUERY_FAIL);
        }
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserId(Long.valueOf(heldCollection.getUserId()));
        var userRes = userFacadeService.query(userQueryRequest);
        if (!userRes.getSuccess() || null == userRes.getData()) {
            throw new CollectionException(CollectionErrorCode.COLLECTION_USER_QUERY_FAIL);
        }
        ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
        chainProcessRequest.setIdentifier(request.getIdentifier());
        chainProcessRequest.setNtfId(heldCollection.getNftId());
        chainProcessRequest.setClassId(collection.getClassId());
        chainProcessRequest.setOwner(userRes.getData().getBlockChainUrl());

        var destroyRes = chainFacadeService.destroy(chainProcessRequest);
        CollectionDestroyResponse response = new CollectionDestroyResponse();
        response.setSuccess(destroyRes.getSuccess());
        if (destroyRes.getSuccess()) {
            //更新藏品持有表
            HeldCollectionDestroyRequest heldCollectionDestroyRequest = new HeldCollectionDestroyRequest();
            BeanUtils.copyProperties(request, heldCollectionDestroyRequest);
            var newHeldCollection = heldCollectionService.destroy(heldCollectionDestroyRequest);
            response.setHeldCollectionId(newHeldCollection.getId());
        }
        return response;
    }

    @Override
    @Facade
    public SingleResponse<CollectionVO> queryById(Long collectionId) {
        // 1. 查询 Caffeine 本地缓存
        Object cachedData = caffeineCache.get(collectionId);
        if (cachedData != null) {
            return (SingleResponse<CollectionVO>) cachedData;
        }

        // 2. 查询 Redis 缓存
        cachedData = redisTemplate.opsForValue().get(collectionId);
        if (cachedData != null) {
            // 更新 Caffeine 本地缓存
            caffeineCache.put(collectionId, (CollectionVO) cachedData);
            return (SingleResponse<CollectionVO>) cachedData;
        }

        // 3. 使用 Redisson 获取锁
        RLock lock = redissonClient.getLock("collectionLock:" + collectionId);
        try {
            long maxWaitTime = 5; // 设置最大等待时间
            long leaseTime = 10; // 设置锁的租约时间
            if (lock.tryLock(maxWaitTime, leaseTime, TimeUnit.SECONDS)) {
                try {
                    // 4. 查询数据库
                    Collection dbData = collectionService.queryById(collectionId);
                    Integer inventory = collectionInventoryRedisService.getInventory(new CollectionInventoryRequest(collectionId.toString()));

                    if (inventory == null) {
                        inventory = 0;
                    }

                    CollectionVO collectionVO = CollectionConvertor.INSTANCE.mapToVo(dbData);
                    collectionVO.setInventory(inventory.longValue());
                    collectionVO.setState(dbData.getState(), dbData.getSaleTime(), inventory.longValue());

                    // 5. 插入 Redis 并更新 Caffeine 缓存
                    redisTemplate.opsForValue().set(String.valueOf(collectionId), collectionVO);
                    caffeineCache.put(collectionId, collectionVO);

                    return (SingleResponse<CollectionVO>) collectionVO;
                } finally {
                    // 确保释放锁
                    lock.unlock();
                }
            } else {
                // 锁获取失败的处理
                throw new RuntimeException("Database query timeout");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            throw new RuntimeException("Thread was interrupted", e);
        }

    }

    @Override
    @Facade
    public SingleResponse<Boolean> preInventoryDeduct(Long collectionId, int quantity, String identifier) {

        CollectionInventoryRequest request = new CollectionInventoryRequest();
        request.setIdentifier(identifier);
        request.setInventory(quantity);
        request.setCollectionId(collectionId.toString());
        CollectionInventoryResponse collectionInventoryResponse = collectionInventoryRedisService.decrease(request);
        if (collectionInventoryResponse.getSuccess()) {

//            //通过netty时间轮，起一个延迟任务，反查订单是否创建成功，如果未创建，则回退库存
//            HashedWheelTimer timer = new HashedWheelTimer();
//
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run(Timeout timeout) throws Exception {
//                    //但是这里不做实现了，主要原因：
//                    //1、藏品模块或者库存模块，不应该冗余太多业务关于订单模块的业务逻辑，
//                    //2、这种检查并不能100%的解决问题，因为一旦这时候服务挂了，或者应用重启了，就丢失了这个任务了。
//                    //所以，这种不一致的情况需要通过对账实现
//                    //要么就是单独搞一个对账中心，要么就是通过对账平台配核对来发现。
//                }
//            };
//
//            //timer.newTimeout(task, 3, TimeUnit.MINUTES);

            return SingleResponse.of(true);
        }

        logger.error("decrease inventory failed, " + JSON.toJSONString(collectionInventoryResponse));
        return SingleResponse.fail(collectionInventoryResponse.getResponseCode(), collectionInventoryResponse.getResponseMessage());
    }

    @Override
    public SingleResponse<CollectionInventoryVO> queryInventory(Long collectionId) {
        Collection collection = collectionService.queryById(collectionId);

        CollectionInventoryVO collectionInventoryVO = new CollectionInventoryVO();
        collectionInventoryVO.setQuantity(collection.getQuantity());
        collectionInventoryVO.setOccupiedInventory(collection.getOccupiedInventory());

        CollectionInventoryRequest collectionInventoryRequest = new CollectionInventoryRequest();
        collectionInventoryRequest.setCollectionId(collectionId.toString());
        Integer saleableInventory = collectionInventoryRedisService.getInventory(collectionInventoryRequest);
        collectionInventoryVO.setSaleableInventory(saleableInventory.longValue());
        return SingleResponse.of(collectionInventoryVO);
    }

    @Override
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request) {
        PageResponse<Collection> colletionPage = collectionService.pageQueryByState(request.getKeyword(), request.getState(), request.getCurrentPage(), request.getPageSize());
        return PageResponse.of(CollectionConvertor.INSTANCE.mapToVo(colletionPage.getDatas()), colletionPage.getTotal(), colletionPage.getPageSize(), request.getCurrentPage());
    }

    @Override
    public PageResponse<HeldCollectionVO> pageQueryHeldCollection(HeldCollectionPageQueryRequest request) {
        PageResponse<HeldCollection> colletionPage = heldCollectionService.pageQueryByState(request);
        return PageResponse.of(HeldCollectionConvertor.INSTANCE.mapToVo(colletionPage.getDatas()), colletionPage.getTotal(), request.getPageSize(), request.getCurrentPage());
    }

    @Override
    public SingleResponse<HeldCollectionVO> queryHeldCollectionById(Long heldCollectionId) {
        HeldCollection transferCollection = heldCollectionService.queryById(heldCollectionId);


        return SingleResponse.of(HeldCollectionConvertor.INSTANCE.mapToVo(transferCollection));
    }


}
