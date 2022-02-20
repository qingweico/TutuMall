package cn.qingweico.dto;

import cn.qingweico.entity.WeChatAuth;
import cn.qingweico.enums.WeChatAuthStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */


public class WeChatAuthExecution {
    /**
     * / 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private int count;

    private WeChatAuth weChatAuth;

    private List<WeChatAuth> weChatAuthList;

    public WeChatAuthExecution() {
    }

    /**
     * 微信授权失败时返回的构造器
     *
     * @param stateEnum WeChatAuthStateEnum
     */
    public WeChatAuthExecution(WeChatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 微信授权成功时返回的构造器
     *
     * @param stateEnum  WeChatAuthStateEnum
     * @param weChatAuth WeChatAuth
     */
    public WeChatAuthExecution(WeChatAuthStateEnum stateEnum, WeChatAuth weChatAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.weChatAuth = weChatAuth;
    }

    /**
     * (批量)微信授权成功时返回的构造器
     *
     * @param stateEnum      WeChatAuthStateEnum
     * @param weChatAuthList weChatAuthList
     */
    public WeChatAuthExecution(WeChatAuthStateEnum stateEnum, List<WeChatAuth> weChatAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.weChatAuthList = weChatAuthList;
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

    public WeChatAuth getWeChatAuth() {
        return weChatAuth;
    }

    public void setWeChatAuth(WeChatAuth weChatAuth) {
        this.weChatAuth = weChatAuth;
    }

    public List<WeChatAuth> getWeChatAuthList() {
        return weChatAuthList;
    }

    public void setWeChatAuthList(List<WeChatAuth> weChatAuthList) {
        this.weChatAuthList = weChatAuthList;
    }
}
