package cn.qingweico.dto;


import cn.qingweico.entity.UserProductMap;
import cn.qingweico.enums.UserProductMapStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/8
 */

public class UserProductMapExecution {
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

    private UserProductMap userProductMap;

    private List<UserProductMap> userProductMapList;

    public UserProductMapExecution() {
    }

    /**
     * 建立用户与其所购买商品之间的映射关系失败时的构造器
     * @param stateEnum 成功或者失败时的状态
     */
    public UserProductMapExecution(UserProductMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 建立用户与其所购买商品之间的映射关系成功时的构造器
     * @param stateEnum 成功或者失败时的状态
     * @param userProductMap 用户与其所购买商品之间的映射关系
     */
    public UserProductMapExecution(UserProductMapStateEnum stateEnum, UserProductMap userProductMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMap = userProductMap;
    }

    /**
     * 批量建立用户与其所购买商品之间的映射关系成功时的构造器
     * @param stateEnum 成功或者失败时的状态
     * @param userProductMapList 用户与其所购买商品之间的映射关系列表
     */
    public UserProductMapExecution(UserProductMapStateEnum stateEnum, List<UserProductMap> userProductMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMapList = userProductMapList;
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

    public UserProductMap getUserProductMap() {
        return userProductMap;
    }

    public void setUserProductMap(UserProductMap userProductMap) {
        this.userProductMap = userProductMap;
    }

    public List<UserProductMap> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }

}
