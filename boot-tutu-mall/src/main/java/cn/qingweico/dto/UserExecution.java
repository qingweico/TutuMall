package cn.qingweico.dto;


import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/15
 */
@Data
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
     * ~
     */
    private User user;

    /**
     * 获取的user列表
     */
    private List<User> userList;

    public UserExecution() {
    }

    /**
     * //
     *
     * @param stateEnum UserStateEnum
     */
    public UserExecution(UserStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * //
     *
     * @param stateEnum UserStateEnum
     * @param user      User
     */
    public UserExecution(UserStateEnum stateEnum, User user) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.user = user;
    }
}