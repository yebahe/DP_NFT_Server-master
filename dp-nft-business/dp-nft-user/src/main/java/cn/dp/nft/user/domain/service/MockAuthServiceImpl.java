package cn.dp.nft.user.domain.service;

/**
 * Mock的认证服务
 *
 * @author yebahe
 */
public class MockAuthServiceImpl implements AuthService {
    @Override
    public boolean checkAuth(String realName, String idCard) {
        return true;
    }
}
