package cn.qingweico.dto;

import cn.qingweico.entity.WeChatAuthRecord;
import cn.qingweico.enums.WeChatAuthStateEnum;
import lombok.Data;

/**
 * @author zqw
 * @date 2020/10/21
 */

@Data
public class WeChatAuthRecordExecution {
    /**
     * / 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private int count;

    private WeChatAuthRecord weChatAuth;

    public WeChatAuthRecordExecution() {
    }

    /**
     * 微信授权失败时返回的构造器
     *
     * @param stateEnum WeChatAuthStateEnum
     */
    public WeChatAuthRecordExecution(WeChatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 微信授权成功时返回的构造器
     *
     * @param stateEnum  WeChatAuthStateEnum
     * @param weChatAuth WeChatAuth
     */
    public WeChatAuthRecordExecution(WeChatAuthStateEnum stateEnum, WeChatAuthRecord weChatAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.weChatAuth = weChatAuth;
    }
}
