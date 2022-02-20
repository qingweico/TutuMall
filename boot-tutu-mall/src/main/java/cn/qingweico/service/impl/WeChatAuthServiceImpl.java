package cn.qingweico.service.impl;


import java.util.Date;

import cn.qingweico.dao.UserDao;
import cn.qingweico.dao.WeChatAuthDao;
import cn.qingweico.dto.WeChatAuthExecution;
import cn.qingweico.entity.User;
import cn.qingweico.entity.WeChatAuth;
import cn.qingweico.enums.WeChatAuthStateEnum;
import cn.qingweico.exception.WeChatAuthOperationException;
import cn.qingweico.service.WeChatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */
@Service
public class WeChatAuthServiceImpl implements WeChatAuthService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatAuthServiceImpl.class);

    WeChatAuthDao weChatAuthDao;

    UserDao userDao;

    @Autowired
    public void setWeChatAuthDao(WeChatAuthDao weChatAuthDao) {
        this.weChatAuthDao = weChatAuthDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public WeChatAuth getWeChatAuthByOpenId(String openId) {
        return weChatAuthDao.queryWeChatInfoByOpenId(openId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WeChatAuthExecution register(WeChatAuth wechatAuth) throws WeChatAuthOperationException {
        //空值判断
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WeChatAuthExecution(WeChatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            //设置创建时间
            wechatAuth.setCreateTime(new Date());
            //如果微信帐号里夹带着用户信息并且用户Id为空, 则认为该用户第一次使用平台(且通过微信登录)
            //则自动创建用户信息
            if (wechatAuth.getUser() != null && wechatAuth.getUser().getUserId() == null) {
                try {
                    wechatAuth.getUser().setCreateTime(new Date());
                    wechatAuth.getUser().setEnableStatus(1);
                    User user = wechatAuth.getUser();
                    int effectedNum = userDao.insertUser(user);
                    wechatAuth.setUser(user);
                    if (effectedNum <= 0) {
                        throw new WeChatAuthOperationException("添加用户信息失败!");
                    }
                } catch (Exception e) {
                    logger.error("添加用户信息失败 : " + e);
                    throw new WeChatAuthOperationException("添加用户信息失败 : " + e.getMessage());
                }
            }
            //创建专属于本平台的微信帐号
            int effectedNum = weChatAuthDao.insertWeChatAuth(wechatAuth);
            if (effectedNum <= 0) {
                throw new WeChatAuthOperationException("帐号创建失败");
            } else {
                return new WeChatAuthExecution(WeChatAuthStateEnum.SUCCESS, wechatAuth);
            }
        } catch (Exception e) {
            logger.error("添加用户信息失败 : " + e);
            throw new WeChatAuthOperationException("添加用户信息失败 : " + e.getMessage());
        }
    }

}
