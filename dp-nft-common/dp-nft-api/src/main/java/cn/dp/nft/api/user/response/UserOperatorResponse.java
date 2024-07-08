package cn.dp.nft.api.user.response;

import cn.dp.nft.api.user.response.data.UserInfo;
import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @author yebahe
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
