package cn.dp.nft.user.domain.service;

/**
 * 认证服务
 *
 * @author yebahe
 */
public interface AuthService {
    /**
     * 校验认证信息
     *
     * @param realName
     * @param idCard
     * @return
     */
    public boolean checkAuth(String realName, String idCard);
}
