package cn.dp.nft.collection.domain.service;

import cn.dp.nft.collection.facade.CollectionCancelSaleRequest;
import cn.dp.nft.collection.facade.CollectionConfirmSaleRequest;
import cn.dp.nft.collection.facade.CollectionTrySaleRequest;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.response.CollectionConfirmSaleResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 藏品服务
 *
 * @author yebahe
 */
public interface CollectionService extends IService<Collection> {
    /**
     * 创建
     *
     * @param request
     * @return
     */
    public Collection create(CollectionChainRequest request);

    /**
     * 尝试售卖
     *
     * @param request
     * @return
     */
    public Boolean trySale(CollectionTrySaleRequest request);

    /**
     * 取消售卖
     *
     * @param request
     * @return
     */
    public Boolean cancelSale(CollectionCancelSaleRequest request);

    /**
     * 确认售卖
     *
     * @param request
     * @return
     */
    public CollectionConfirmSaleResponse confirmSale(CollectionConfirmSaleRequest request);

    /**
     * 查询
     * @param collectionId
     * @return
     */
    public Collection queryById(Long collectionId);

    /**
     * 分页查询
     *
     * @param keyWord
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageResponse<Collection> pageQueryByState(String keyWord, String state, int currentPage, int pageSize);

}
