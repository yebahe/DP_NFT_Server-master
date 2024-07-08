package cn.dp.nft.user.domain.entity.convertor;

import cn.dp.nft.api.user.constant.UserStateEnum;
import cn.dp.nft.api.user.response.data.UserInfo;
import cn.dp.nft.user.domain.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-07T17:49:05+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
public class UserConvertorImpl implements UserConvertor {

    @Override
    public UserInfo mapToVo(User request) {
        if ( request == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        if ( request.getId() != null ) {
            userInfo.setUserId( request.getId() );
        }
        if ( request.getNickName() != null ) {
            userInfo.setNickName( request.getNickName() );
        }
        if ( request.getTelephone() != null ) {
            userInfo.setTelephone( request.getTelephone() );
        }
        if ( request.getState() != null ) {
            userInfo.setState( request.getState().name() );
        }
        if ( request.getProfilePhotoUrl() != null ) {
            userInfo.setProfilePhotoUrl( request.getProfilePhotoUrl() );
        }
        if ( request.getBlockChainUrl() != null ) {
            userInfo.setBlockChainUrl( request.getBlockChainUrl() );
        }
        if ( request.getBlockChainPlatform() != null ) {
            userInfo.setBlockChainPlatform( request.getBlockChainPlatform() );
        }
        if ( request.getCertification() != null ) {
            userInfo.setCertification( request.getCertification() );
        }
        if ( request.getUserRole() != null ) {
            userInfo.setUserRole( request.getUserRole() );
        }

        return userInfo;
    }

    @Override
    public User mapToEntity(UserInfo request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        if ( request.getUserId() != null ) {
            user.setId( request.getUserId() );
        }
        if ( request.getNickName() != null ) {
            user.setNickName( request.getNickName() );
        }
        if ( request.getState() != null ) {
            user.setState( Enum.valueOf( UserStateEnum.class, request.getState() ) );
        }
        if ( request.getTelephone() != null ) {
            user.setTelephone( request.getTelephone() );
        }
        if ( request.getProfilePhotoUrl() != null ) {
            user.setProfilePhotoUrl( request.getProfilePhotoUrl() );
        }
        if ( request.getBlockChainUrl() != null ) {
            user.setBlockChainUrl( request.getBlockChainUrl() );
        }
        if ( request.getBlockChainPlatform() != null ) {
            user.setBlockChainPlatform( request.getBlockChainPlatform() );
        }
        if ( request.getCertification() != null ) {
            user.setCertification( request.getCertification() );
        }
        if ( request.getUserRole() != null ) {
            user.setUserRole( request.getUserRole() );
        }

        return user;
    }
}
