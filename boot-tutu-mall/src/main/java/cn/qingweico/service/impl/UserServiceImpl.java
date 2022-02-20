package cn.qingweico.service.impl;


import cn.qingweico.dao.UserDao;
import cn.qingweico.dto.UserExecution;
import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;
import cn.qingweico.exception.UserOperationException;
import cn.qingweico.service.UserService;

import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/16
 */

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.queryUserById(userId);

    }


    /**
     * 列出除了超级管理员之外所有的的用户
     * @param pageIndex 起始索引
     * @param pageSize  每页数量
     * @return UserExecution
     */
    @Override
    public UserExecution getUserList(int pageIndex, int pageSize) {
        // 页转行
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
        // 获取用户信息列表
        List<User> userList = userDao.queryUserList(rowIndex, pageSize);
        int count = userDao.queryUserCount();
        UserExecution userExecution = new UserExecution();
        if (userList != null) {
            userExecution.setUserList(userList);
            userExecution.setCount(count);
        } else {
            userExecution.setState(UserStateEnum.INNER_ERROR.getState());
        }
        return userExecution;
    }

    /**
     * 修改用户的信息
     * @param user 用户信息
     * @return UserExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserExecution modifyUser(User user) {
        // 空值判断, 判断用户Id是否为空
        if (user == null || user.getUserId() == null) {
            return new UserExecution(UserStateEnum.EMPTY);
        } else {
            try {
                // 更新用户信息
                int effectedNum = userDao.updateUser(user);
                if (effectedNum <= 0) {
                    return new UserExecution(UserStateEnum.INNER_ERROR);
                } else {
                    user = userDao.queryUserById(user.getUserId());
                    return new UserExecution(UserStateEnum.SUCCESS, user);
                }
            } catch (Exception e) {
                throw new UserOperationException("更新用户信息失败! " + e.getMessage());
            }
        }
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return UserExecution
     */
    @Override
    public UserExecution getUserByName(String username) {
        if (!"".equals(username)) {
            try {
                User user = userDao.queryUserByName(username);
                if (user == null) {
                    return new UserExecution(UserStateEnum.NO_EXIST);
                } else {
                    return new UserExecution(UserStateEnum.SUCCESS, user);
                }
            } catch (Exception e) {
                throw new UserOperationException("查询用户信息失败! " + e.getMessage());
            }
        } else {
            return new UserExecution(UserStateEnum.INNER_ERROR);
        }
    }
}
