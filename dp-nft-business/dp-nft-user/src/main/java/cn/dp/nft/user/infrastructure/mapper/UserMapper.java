package cn.dp.nft.user.infrastructure.mapper;

import cn.dp.nft.user.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * user mapper
 * @author yebahe
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findById(long id);

    /**
     * 根据昵称查询用户
     *
     * @param nickname
     * @return
     */
    User findByNickname(String nickname);

    /**
     * 根据手机号查询用户
     *
     * @param telephone
     * @return
     */
    User findByTelephone(String telephone);

    /**
     * 根据昵称和密码查询用户
     *
     * @param nickName
     * @param passwordHash
     * @return
     */
    User findByNameAndPass(String nickName, String passwordHash);
}
