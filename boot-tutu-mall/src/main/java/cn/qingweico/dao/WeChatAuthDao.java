package cn.qingweico.dao;

import cn.qingweico.entity.WeChatAuth;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */
@Repository
public interface WeChatAuthDao {
    /**
     * 通过openId查询对应本平台的微信帐号
     *
     * @param openId openId
     * @return WeChatAuth
     */
    WeChatAuth queryWeChatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信帐号
     *
     * @param weChatAuth WeChatAuth
     * @return effectNum
     */
    int insertWeChatAuth(WeChatAuth weChatAuth);

}
