package cn.qingweico.service;

import cn.qingweico.dto.WeChatAuthRecordExecution;
import cn.qingweico.entity.WeChatAuthRecord;

/**
 * @author zqw
 * @date 2020/10/21
 */
public interface WeChatAuthService {

    /**
     * 通过openId查找平台对应的微信帐号
     *
     * @param openId openId
     * @return WeChatAuth
     */
    WeChatAuthRecord getWeChatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信帐号
     *
     * @param weChatAuth WeChatAuth
     * @return WeChatAuthExecution
     */
    WeChatAuthRecordExecution register(WeChatAuthRecord weChatAuth);


}
