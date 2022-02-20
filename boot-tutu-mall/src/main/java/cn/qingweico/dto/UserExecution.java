package cn.qingweico.dto;


import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

public class UserExecution {

    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 店铺数量
     */
    private int count;

    /**
     * 操作的user（增删改店铺的时候用）
     */
    private User user;

    /**
     * 获取的user列表(查询店铺列表的时候用)
     */
    private List<User> userList;

    public UserExecution() {
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum UserStateEnum
     */
    public UserExecution(UserStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum UserStateEnum
     * @param user      User
     */
    public UserExecution(UserStateEnum stateEnum, User user) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.user = user;
    }

    /**
     * 批量操作失败时返回的构造器
     *
     * @param stateEnum UserStateEnum
     * @param userList  List<User>
     */
    public UserExecution(UserStateEnum stateEnum, List<User> userList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userList = userList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}