package cn.qingweico.dto;

import cn.qingweico.entity.UserShopMap;
import cn.qingweico.enums.UserShopMapStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/14
 */

public class UserShopMapExecution {
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
     * 操作的UserShopMap
     */
    private UserShopMap userShopMap;

    /**
     * 授权列表（查询专用）
     */
    private List<UserShopMap> userShopMapList;

    public UserShopMapExecution() {
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum 错误状态信息
     */
    public UserShopMapExecution(UserShopMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum   状态信息
     * @param userShopMap userShopMap
     */
    public UserShopMapExecution(UserShopMapStateEnum stateEnum, UserShopMap userShopMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    /**
     * 批量操作成功时返回的构造器
     *
     * @param stateEnum       状态信息
     * @param userShopMapList userShopMapList
     */
    public UserShopMapExecution(UserShopMapStateEnum stateEnum, List<UserShopMap> userShopMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }

}
