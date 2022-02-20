package cn.qingweico.dto;

import cn.qingweico.entity.UserAwardMap;
import cn.qingweico.enums.UserAwardMapStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

public class UserAwardMapExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 授权数
     */
    private Integer count;

    /**
     * 操作的UserAwardMap
     */
    private UserAwardMap userAwardMap;

    /**
     * 用户奖品信息映射列表（查询专用）
     */
    private List<UserAwardMap> userAwardMapList;

    public UserAwardMapExecution() {
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum UserAwardMapStateEnum
     */
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum    UserAwardMapStateEnum
     * @param userAwardMap UserAwardMap
     */
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, UserAwardMap userAwardMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    /**
     * 批量操作成功时返回的构造器
     *
     * @param stateEnum        UserAwardMapStateEnum
     * @param userAwardMapList List<UserAwardMap>
     */
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMapList = userAwardMapList;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }

}
