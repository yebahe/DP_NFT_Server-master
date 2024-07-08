package cn.dp.nft.collection.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.api.collection.model.HeldCollectionVO;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import cn.dp.nft.api.collection.request.CollectionPageQueryRequest;
import cn.dp.nft.api.collection.request.HeldCollectionPageQueryRequest;
import cn.dp.nft.api.collection.response.CollectionChainResponse;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.base.response.SingleResponse;
import cn.dp.nft.collection.domain.request.CollectionInventoryRequest;
import cn.dp.nft.collection.domain.response.CollectionInventoryResponse;
import cn.dp.nft.collection.domain.service.impl.redis.CollectionInventoryRedisService;
import cn.dp.nft.web.util.MultiResultConvertor;
import cn.dp.nft.web.vo.MultiResult;
import cn.dp.nft.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wswyb001
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    private CollectionFacadeService collectionFacadeService;

    @Autowired
    private CollectionInventoryRedisService collectionInventoryRedisService;

    /**
     * 这是在后台功能上线前，提供的一个临时创建藏品的接口。
     * @return
     */
    @GetMapping("/chain")
    public Result<Boolean> chain() {
        CollectionChainRequest request = new CollectionChainRequest();
        request.setIdentifier(String.valueOf(new Date().getTime()));
        request.setClassId("id" + new Date().getTime());
        request.setName("测试藏品");
        request.setQuantity(100L);
        request.setSaleTime(new Date());
        request.setPrice(BigDecimal.TEN);
        request.setCover("https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF");
        CollectionChainResponse response = collectionFacadeService.chain(request);
        if (!response.getSuccess()) {
            return Result.error(response.getResponseCode(), response.getResponseMessage());
        }
        CollectionInventoryRequest inventoryRequest = new CollectionInventoryRequest();
        inventoryRequest.setCollectionId(response.getCollectionId().toString());
        inventoryRequest.setInventory(100);
        inventoryRequest.setIdentifier(response.getCollectionId().toString());
        CollectionInventoryResponse inventoryResponse = collectionInventoryRedisService.init(inventoryRequest);
        return Result.success(inventoryResponse.getSuccess());
    }

    /**
     * 藏品列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/collectionList")
    public MultiResult<CollectionVO> collectionList(@NotBlank String state, String keyWord, int pageSize, int currentPage) {
        CollectionPageQueryRequest collectionPageQueryRequest = new CollectionPageQueryRequest();
        collectionPageQueryRequest.setState(state);
        collectionPageQueryRequest.setKeyword(keyWord);
        collectionPageQueryRequest.setCurrentPage(currentPage);
        collectionPageQueryRequest.setPageSize(pageSize);
        PageResponse<CollectionVO> pageResponse = collectionFacadeService.pageQuery(collectionPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    /**
     * 藏品详情
     *
     * @param
     * @return 结果
     */
    @GetMapping("/collectionInfo")
    public Result<CollectionVO> collectionInfo(@NotBlank String collectionId) {
        SingleResponse<CollectionVO> singleResponse = collectionFacadeService.queryById(Long.valueOf(collectionId));
        return Result.success(singleResponse.getData());
    }

    /**
     * 用户持有藏品列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/heldCollectionList")
    public MultiResult<HeldCollectionVO> heldCollectionList(String keyword, String state, int pageSize, int currentPage) {
        String userId = (String) StpUtil.getLoginId();
        HeldCollectionPageQueryRequest heldCollectionPageQueryRequest = new HeldCollectionPageQueryRequest();
        heldCollectionPageQueryRequest.setState(state);
        heldCollectionPageQueryRequest.setUserId(userId);
        heldCollectionPageQueryRequest.setCurrentPage(currentPage);
        heldCollectionPageQueryRequest.setPageSize(pageSize);
        heldCollectionPageQueryRequest.setKeyword(keyword);
        PageResponse<HeldCollectionVO> pageResponse = collectionFacadeService.pageQueryHeldCollection(heldCollectionPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    /**
     * 用户持有藏品详情
     *
     * @param
     * @return 结果
     */
    @GetMapping("/heldCollectionInfo")
    public Result<HeldCollectionVO> heldCollectionInfo(@NotBlank String heldCollectionId) {
        SingleResponse<HeldCollectionVO> singleResponse = collectionFacadeService.queryHeldCollectionById(Long.valueOf(heldCollectionId));
        return Result.success(singleResponse.getData());
    }


}
