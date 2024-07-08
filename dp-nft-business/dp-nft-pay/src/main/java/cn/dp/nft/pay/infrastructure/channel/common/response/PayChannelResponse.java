package cn.dp.nft.pay.infrastructure.channel.common.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wswyb001
 */
@Setter
@Getter
public class PayChannelResponse extends BaseResponse {
    protected String payUrl;
}
