package cn.dp.nft.api.user.request;

import cn.dp.nft.base.request.BaseRequest;
import lombok.*;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private String telephone;

    private Long userId;

}
