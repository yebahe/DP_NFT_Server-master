package cn.dp.nft.api.user.constant;

/**
 * 用户操作类型
 * @author yebahe
 */
public enum UserOperateTypeEnum {
    /**
     * 冻结
     */
    FREEZE,

    /**
     * 解冻
     */
    UNFREEZE,

    /**
     * 登录
     */
    LOGIN,
    /**
     * 注册
     */
    REGISTER,
    /**
     * 激活
     */
    ACTIVE,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 修改信息
     */
    MODIFY
    ;
}
