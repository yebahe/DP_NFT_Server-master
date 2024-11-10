package cn.dp.nft.collection.controller;

import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.entity.CollectionItem;
import cn.dp.nft.collection.domain.service.impl.cache.CacheCollectionService;
import cn.dp.nft.web.vo.Result;
import lombok.AllArgsConstructor;
import org.apache.dubbo.auth.v1alpha1.Ca;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  缓存商品信息
 */
@AllArgsConstructor
@RestController
@RequestMapping("/cache/collections")
public class CollectionCacheController {
    private final CacheCollectionService cacheCollectionService;

    /**
     *
     * @param collectionItem 缓存基本信息
     */
    @RequestMapping("/insert/basicInfo")
    public void insertCollectionBasicInfo(@RequestBody CollectionItem collectionItem) {
        cacheCollectionService.insertBasicInfo(collectionItem);

    }
    public Result<CollectionItem> insertCollection(@RequestBody Collection collection) {
        CollectionItem basicInfo = cacheCollectionService.getBasicInfo(collection.getId());
        return Result.success(basicInfo);

    }
}
