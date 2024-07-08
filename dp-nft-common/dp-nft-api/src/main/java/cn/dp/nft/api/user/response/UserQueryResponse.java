package cn.dp.nft.api.user.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
public class UserQueryResponse<T> extends BaseResponse {

    private static final long serialVersionUID = 1L;

    private T data;
}
