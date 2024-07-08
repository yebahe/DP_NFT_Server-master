package cn.dp.nft.goods.facade.service;

import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.goods.constant.GoodsType;
import cn.dp.nft.api.goods.model.BaseGoodsVO;
import cn.dp.nft.api.goods.service.GoodsFacadeService;
import cn.dp.nft.base.response.SingleResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class GoodsFacadeServiceImpl implements GoodsFacadeService {

    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @Override
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType) {
        return switch (goodsType) {
            case GoodsType.COLLECTION -> {
                SingleResponse<CollectionVO> response = collectionFacadeService.queryById(Long.valueOf(goodsId));
                if (response.getSuccess()) {
                    yield response.getData();
                }
                yield null;
            }
            default -> throw new UnsupportedOperationException("unsupport goods type");
        };
    }
}
