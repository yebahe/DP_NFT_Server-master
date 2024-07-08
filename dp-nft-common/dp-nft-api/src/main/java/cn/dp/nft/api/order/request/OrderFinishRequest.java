package cn.dp.nft.api.order.request;

import cn.dp.nft.api.order.constant.TradeOrderEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class OrderFinishRequest extends BaseOrderUpdateRequest {

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.FINISH;
    }
}

