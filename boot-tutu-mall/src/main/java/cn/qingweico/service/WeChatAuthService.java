package cn.qingweico.service;

import cn.qingweico.dto.WeChatAuthExecution;
import cn.qingweico.entity.WeChatAuth;
import cn.qingweico.exception.WeChatAuthOperationException;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */
public interface WeChatAuthService {

    /**
     * 通过openId查找平台对应的微信帐号
     *
     * @param openId openId
     * @return WeChatAuth
     */
    WeChatAuth getWeChatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信帐号
     *
     * @param weChatAuth WeChatAuth
     * @return WeChatAuthExecution
     * @throws WeChatAuthOperationException WeChatAuthOperationException
     */
    WeChatAuthExecution register(WeChatAuth weChatAuth) throws WeChatAuthOperationException;


}
