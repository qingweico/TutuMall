package cn.qingweico.dao;

import cn.qingweico.entity.WeChatAuthRecord;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
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
    WeChatAuthRecord queryWeChatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信帐号
     *
     * @param weChatAuth WeChatAuth
     * @return effectNum
     */
    int insertWeChatAuth(WeChatAuthRecord weChatAuth);

}
