package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/16
 */

@Repository
public interface UserDao {
    /**
     * 根据查询条件分页返回用户信息列表
     *
     * @param rowIndex 起始索引
     * @param pageSize 每页的数量
     * @return 用户信息列表
     */
    List<User> queryUserList(@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
    /**
     * 通过用户Id查询用户
     *
     * @param userId 用户id
     * @return 用户信息
     */
    User queryUserById(long userId);

    /**
     * 添加用户信息
     *
     * @param user user
     * @return effectNum
     */
    int insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user user
     * @return effectNum
     */
    int updateUser(User user);

    /**
     * 通过用户姓名查询用户信息
     *
     * @param username 用户姓名
     * @return User
     */
    User queryUserByName(String username);

}
