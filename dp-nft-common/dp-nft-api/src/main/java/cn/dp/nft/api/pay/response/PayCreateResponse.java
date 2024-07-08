package cn.dp.nft.api.pay.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class PayCreateResponse extends BaseResponse {

    private String payOrderId;

    private String payUrl;
}
