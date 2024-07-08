package cn.dp.nft.api.user.constant;

/**
 * 用户状态
 *
 * @author yebahe
 */
public enum UserStateEnum {
    /**
     * 创建成功
     */
    INIT,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 上链成功
     */
    ACTIVE,

    /**
     * 冻结
     */
    FROZEN;
}
