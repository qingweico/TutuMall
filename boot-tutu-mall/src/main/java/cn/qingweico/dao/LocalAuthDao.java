package cn.qingweico.dao;


import java.util.Date;

import cn.qingweico.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/11/10
 */

@Repository
public interface LocalAuthDao {

    /**
     * 通过帐号和密码查询对应信息
     *
     * @param username 用户名
     * @param password 密码
     * @return LocalAuth
     */
    LocalAuth queryLocalByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户id查询对应localAuth
     *
     * @param userId 用户id
     * @return LocalAuth
     */
    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    /**
     * 添加平台帐号
     *
     * @param localAuth LocalAuth
     * @return effectNum
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过userId,username,password更改密码
     *
     * @param userId       用户id
     * @param username     用户名
     * @param password     密码
     * @param newPassword  新密码
     * @param lastEditTime 最新修改的时间
     * @return effectNum
     */
    int updateLocalAuth(@Param("userId") Integer userId, @Param("username") String username,
                        @Param("password") String password, @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}
