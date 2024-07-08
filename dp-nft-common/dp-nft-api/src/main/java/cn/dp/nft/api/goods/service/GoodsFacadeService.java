package cn.dp.nft.api.goods.service;

import cn.dp.nft.api.goods.constant.GoodsType;
import cn.dp.nft.api.goods.model.BaseGoodsVO;

/**
 * 商品服务
 *
 * @author yebahe
 */
public interface GoodsFacadeService {

    /**
     * 获取商品
     *
     * @param goodsId
     * @param goodsType
     * @return
     */
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType);

}
