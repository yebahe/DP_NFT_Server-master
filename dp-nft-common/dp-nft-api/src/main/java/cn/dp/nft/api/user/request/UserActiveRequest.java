package cn.dp.nft.api.user.request;

import cn.dp.nft.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveRequest extends BaseRequest {

    private Long userId;
    private String blockChainPlatform;
    private String blockChainUrl;

}
