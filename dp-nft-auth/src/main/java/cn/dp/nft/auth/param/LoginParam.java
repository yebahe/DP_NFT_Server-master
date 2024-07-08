package cn.dp.nft.auth.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Setter
@Getter
public class LoginParam extends RegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
