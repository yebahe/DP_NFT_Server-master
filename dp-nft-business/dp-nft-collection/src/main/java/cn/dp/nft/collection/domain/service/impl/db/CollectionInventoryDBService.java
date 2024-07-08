package cn.dp.nft.collection.domain.service.impl.db;

import cn.dp.nft.collection.domain.response.CollectionInventoryResponse;
import cn.dp.nft.collection.domain.service.CollectionInventoryService;
import cn.dp.nft.collection.domain.request.CollectionInventoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 藏品库存服务-基于数据库
 *
 * @author yebahe
 */
@Service
public class CollectionInventoryDBService implements CollectionInventoryService {

    @Override
    public CollectionInventoryResponse init(CollectionInventoryRequest request) {
        //todo
        return null;
    }

    @Override
    public Integer getInventory(CollectionInventoryRequest request) {
        //todo
        return null;
    }

    @Override
    public CollectionInventoryResponse decrease(CollectionInventoryRequest request) {
        //todo
        return null;
    }

    @Override
    public List<Object> getInventoryDecreaseLogs(CollectionInventoryRequest request) {
        //todo
        return null;
    }

    @Override
    public CollectionInventoryResponse increase(CollectionInventoryRequest request) {
        //todo
        return null;
    }

    @Override
    public void invalid(CollectionInventoryRequest request) {
        //todo
    }
}
