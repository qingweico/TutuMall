package cn.qingweico.service.impl;


import cn.qingweico.dao.UserDao;
import cn.qingweico.dto.UserExecution;
import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;
import cn.qingweico.service.UserService;

import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2020/10/16
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public User getUserById(Long userId) {
        return userDao.queryUserById(userId);

    }

    /**
     * 列出除了admin之外所有的的用户
     *
     * @param page     起始索引
     * @param pageSize 每页数量
     * @return UserExecution
     */
    @Override
    public UserExecution getUserList(int page, int pageSize) {
        // 页转行
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 获取用户信息列表
        List<User> userList = userDao.queryUserList(rowIndex, pageSize);
        UserExecution userExecution = new UserExecution();
        if (userList != null) {
            userExecution.setUserList(userList);
        } else {
            userExecution.setStateInfo(UserStateEnum.INNER_ERROR.getStateInfo());
        }
        return userExecution;
    }

    /**
     * 修改用户的信息
     *
     * @param user 用户信息
     * @return UserExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserExecution modifyUser(User user) {
        if (userDao.updateUser(user) <= 0) {
            return new UserExecution(UserStateEnum.INNER_ERROR);
        } else {
            return new UserExecution(UserStateEnum.SUCCESS);
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return UserExecution
     */
    @Override
    public UserExecution getUserByName(String username) {
        User user = userDao.queryUserByName(username);
        if (user == null) {
            return new UserExecution(UserStateEnum.NO_EXIST);
        } else {
            return new UserExecution(UserStateEnum.SUCCESS);
        }
    }
}
