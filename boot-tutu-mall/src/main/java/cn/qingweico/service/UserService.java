package cn.qingweico.service;

import cn.qingweico.dto.UserExecution;
import cn.qingweico.entity.User;

/**
 * @author zqw
 * @date 2020/10/15
 */
public interface UserService {

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 根据查询条件分页返回用户信息列表
     *
     * @param page     起始索引
     * @param pageSize 每页数量
     * @return UserExecution
     */
    UserExecution getUserList(int page, int pageSize);

    /**
     * 根据传入的user修改对应的用户信息
     *
     * @param user 用户信息
     * @return UserExecution
     */
    UserExecution modifyUser(User user);

    /**
     * 根据传入的用户姓名查询用户的信息
     *
     * @param username 用户名
     * @return UserExecution
     */
    UserExecution getUserByName(String username);
}
