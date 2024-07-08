package cn.dp.nft.user.facade;

import cn.dp.nft.api.user.request.UserActiveRequest;
import cn.dp.nft.api.user.request.UserAuthRequest;
import cn.dp.nft.api.user.request.UserModifyRequest;
import cn.dp.nft.api.user.request.UserQueryRequest;
import cn.dp.nft.api.user.request.UserRegisterRequest;
import cn.dp.nft.api.user.response.UserOperatorResponse;
import cn.dp.nft.api.user.response.UserQueryResponse;
import cn.dp.nft.api.user.response.data.UserInfo;
import cn.dp.nft.api.user.service.UserFacadeService;
import cn.dp.nft.rpc.facade.Facade;
import cn.dp.nft.user.domain.entity.User;
import cn.dp.nft.user.domain.entity.convertor.UserConvertor;
import cn.dp.nft.user.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Facade
    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {
        User user = null;
        if (null != userQueryRequest.getUserId()) {
            user = userService.findById(userQueryRequest.getUserId());
        }

        if (StringUtils.isNotBlank(userQueryRequest.getTelephone())) {
            user = userService.findByTelephone(userQueryRequest.getTelephone());
        }

        UserQueryResponse<UserInfo> response = new UserQueryResponse();
        response.setSuccess(true);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        response.setData(userInfo);
        return response;
    }

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.getTelephone());
    }

    @Override
    @Facade
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        return userService.modify(userModifyRequest);
    }

    @Override
    @Facade
    public UserOperatorResponse auth(UserAuthRequest userAuthRequest) {
        return userService.auth(userAuthRequest);
    }

    @Override
    @Facade
    public UserOperatorResponse active(UserActiveRequest userActiveRequest) {
        return userService.active(userActiveRequest);
    }
}
