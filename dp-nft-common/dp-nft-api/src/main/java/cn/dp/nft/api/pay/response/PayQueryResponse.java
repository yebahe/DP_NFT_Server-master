package cn.dp.nft.api.pay.response;

import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PayQueryResponse extends BaseResponse {

    private List<PayOrderVO> payOrders;
}
