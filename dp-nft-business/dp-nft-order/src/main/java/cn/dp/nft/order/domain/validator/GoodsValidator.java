package cn.dp.nft.order.domain.validator;

import cn.dp.nft.order.domain.exception.OrderException;
import cn.dp.nft.api.goods.model.BaseGoodsVO;
import cn.dp.nft.api.goods.service.GoodsFacadeService;
import cn.dp.nft.api.order.request.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.dp.nft.api.order.constant.OrderErrorCode.GOODS_NOT_AVAILABLE;
import static cn.dp.nft.api.order.constant.OrderErrorCode.GOODS_PRICE_CHANGED;

/**
 * 商品校验器
 *
 * @author yebahe
 */
@Component
public class GoodsValidator extends BaseOrderCreateValidator {

    @Autowired
    private GoodsFacadeService goodsFacadeService;

    @Override
    protected void doValidate(OrderCreateRequest request) throws OrderException {
        BaseGoodsVO baseGoodsVO = goodsFacadeService.getGoods(request.getGoodsId(), request.getGoodsType());

        if (!baseGoodsVO.available()) {
            throw new OrderException(GOODS_NOT_AVAILABLE);
        }

        if (baseGoodsVO.getPrice().compareTo(request.getItemPrice()) != 0) {
            throw new OrderException(GOODS_PRICE_CHANGED);
        }
    }
}
