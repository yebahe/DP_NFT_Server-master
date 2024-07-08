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
public class UserRegisterRequest extends BaseRequest {

    private String telephone;

}
