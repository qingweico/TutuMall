package cn.qingweico.service.impl;


import java.util.Date;

import cn.qingweico.dao.UserDao;
import cn.qingweico.dao.WeChatAuthDao;
import cn.qingweico.dto.WeChatAuthRecordExecution;
import cn.qingweico.entity.User;
import cn.qingweico.entity.WeChatAuthRecord;
import cn.qingweico.enums.WeChatAuthStateEnum;
import cn.qingweico.service.WeChatAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/10/21
 */
@Slf4j
@Service
public class WeChatAuthServiceImpl implements WeChatAuthService {


    @Resource
    WeChatAuthDao weChatAuthDao;
    @Resource
    UserDao userDao;


    @Override
    public WeChatAuthRecord getWeChatAuthByOpenId(String openId) {
        return weChatAuthDao.queryWeChatInfoByOpenId(openId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WeChatAuthRecordExecution register(WeChatAuthRecord wechatAuth) {
        wechatAuth.setCreateTime(new Date());
        if (wechatAuth.getUser() != null && wechatAuth.getUser().getId() == null) {
            wechatAuth.getUser().setCreateTime(new Date());
            wechatAuth.getUser().setEnableStatus(1);
            User user = wechatAuth.getUser();
            int effectedNum = userDao.insertUser(user);
            wechatAuth.setUser(user);
            if (effectedNum <= 0) {
                return new WeChatAuthRecordExecution(WeChatAuthStateEnum.INNER_ERROR);
            }
        }
        //创建专属于本平台的微信帐号
        int effectedNum = weChatAuthDao.insertWeChatAuth(wechatAuth);
        if (effectedNum <= 0) {
            return new WeChatAuthRecordExecution(WeChatAuthStateEnum.INNER_ERROR);
        } else {
            return new WeChatAuthRecordExecution(WeChatAuthStateEnum.SUCCESS);
        }
    }

}
