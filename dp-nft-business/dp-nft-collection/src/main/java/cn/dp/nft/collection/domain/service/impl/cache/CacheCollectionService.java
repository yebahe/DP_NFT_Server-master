package cn.dp.nft.collection.domain.service.impl.cache;

import cn.dp.nft.collection.domain.entity.CollectionItem;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 *  藏品缓存的信息
 */
@Service
@AllArgsConstructor
public class CacheCollectionService {
    private final RedisTemplate<String,Object> redisTemplate;
    public void insertBasicInfo(CollectionItem collectionItem) {
        String key = "prefix" + collectionItem.getId();
        String value = collectionItem.toString();
        redisTemplate.opsForValue().set(key,value);
    }

    public CollectionItem getBasicInfo(String collectionId) {
        String  key = "prefix" + collectionId;
        CollectionItem collectionItem = (CollectionItem) redisTemplate.opsForValue().get(key);
        return collectionItem;
    }
}
