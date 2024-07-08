package cn.dp.nft.order.wrapper;

import cn.dp.nft.api.collection.model.CollectionInventoryVO;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.goods.constant.GoodsType;
import cn.dp.nft.api.goods.model.BaseGoodsInventoryVO;
import cn.dp.nft.api.order.request.OrderCreateRequest;
import cn.dp.nft.base.response.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yebahe
 */
@Service
public class InventoryWrapperService {

    @Autowired
    private CollectionFacadeService collectionFacadeService;

    public Boolean preDeduct(OrderCreateRequest orderCreateRequest) {
        GoodsType goodsType = orderCreateRequest.getGoodsType();
        String preDeductIdentifier = orderCreateRequest.getBuyerId() + "_" + orderCreateRequest.getIdentifier() + "_" + orderCreateRequest.getItemCount();
        return switch (goodsType) {
            case COLLECTION -> {
                SingleResponse<Boolean> response = collectionFacadeService.preInventoryDeduct(Long.valueOf(orderCreateRequest.getGoodsId()), orderCreateRequest.getItemCount(), preDeductIdentifier);
                if (response.getSuccess()) {
                    yield response.getData();
                }
                yield Boolean.FALSE;
            }
            default -> throw new UnsupportedOperationException("unsupport goods type");
        };
    }

    public BaseGoodsInventoryVO queryInventory(OrderCreateRequest orderCreateRequest) {

        GoodsType goodsType = orderCreateRequest.getGoodsType();
        return switch (goodsType) {
            case COLLECTION -> {
                SingleResponse<CollectionInventoryVO> response = collectionFacadeService.queryInventory(Long.valueOf(orderCreateRequest.getGoodsId()));
                if (response.getSuccess()) {
                    yield response.getData();
                }
                yield null;
            }
            default -> throw new UnsupportedOperationException("unsupport goods type");
        };
    }
}
