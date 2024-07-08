package cn.dp.nft.auth.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Setter
@Getter
public class RegisterParam {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
