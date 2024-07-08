package cn.dp.nft.collection.domain.service;

import cn.dp.nft.collection.CollectionBaseTest;
import cn.dp.nft.api.collection.constant.CollectionSaleBizType;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.response.CollectionConfirmSaleResponse;
import cn.dp.nft.collection.facade.CollectionConfirmSaleRequest;
import cn.dp.nft.collection.facade.CollectionTrySaleRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public class CollectionServiceTest extends CollectionBaseTest {

    @Autowired
    private CollectionService collectionService;

    @Test
    public void createTest() {
        CollectionChainRequest request = new CollectionChainRequest();
        request.setIdentifier("123456");
        request.setClassId("classId");
        request.setName("name");
        request.setCover("cover");
        request.setPrice(BigDecimal.ONE);
        request.setQuantity(100L);
        request.setCreateTime(new Date());
        request.setSaleTime(new Date());
        Collection collection = collectionService.create(request);
        Assert.assertTrue(collection.getId() != null);
        var queRes = collectionService.queryById(collection.getId());
        Assert.assertTrue(queRes.getId() != null);

    }

    @Test
    public void saleTest() {
        CollectionChainRequest request = new CollectionChainRequest();
        request.setIdentifier("1234567");
        request.setClassId("classId1");
        request.setName("name");
        request.setCover("cover");
        request.setPrice(BigDecimal.ONE);
        request.setQuantity(100L);
        request.setCreateTime(new Date());
        request.setSaleTime(new Date());
        Collection collection = collectionService.create(request);
        Assert.assertTrue(collection.getId() != null);
        CollectionTrySaleRequest collectionTrySaleRequest = new CollectionTrySaleRequest("test123", collection.getId(), 1l);
        boolean tryRes = collectionService.trySale(collectionTrySaleRequest);
        Assert.assertTrue(tryRes);
        var queRes = collectionService.queryById(collection.getId());
        Assert.assertTrue(queRes.getSaleableInventory() == 99L);
        CollectionConfirmSaleRequest collectionSaleConfirm = new CollectionConfirmSaleRequest("676776", collection.getId(), 1l, "23123", CollectionSaleBizType.PRIMARY_TRADE.name(), "321321", "name", "cover", BigDecimal.ONE);
        //TODO 返回藏品信息保存失败
        CollectionConfirmSaleResponse confirmRes = collectionService.confirmSale(collectionSaleConfirm);

    }
}
