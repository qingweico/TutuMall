package cn.qingweico.service.impl;

import java.util.Date;

import cn.qingweico.dao.LocalAuthDao;
import cn.qingweico.dto.LocalAuthExecution;
import cn.qingweico.entity.LocalAuth;
import cn.qingweico.enums.LocalAuthStateEnum;
import cn.qingweico.exception.LocalAuthOperationException;
import cn.qingweico.service.LocalAuthService;
import cn.qingweico.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 周庆伟
 * @date 2020/11/11
 */

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    private LocalAuthDao localAuthDao;

    @Autowired
    public void setLocalAuthDao(LocalAuthDao localAuthDao) {
        this.localAuthDao = localAuthDao;
    }

    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPassword(username, MD5.getMd5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        // 空值判断, 传入的localAuth 帐号密码, 用户信息特别是userId不能为空, 否则直接返回错误
        if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
                || localAuth.getUser() == null || localAuth.getUser().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        // 查询此用户是否已绑定过平台帐号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getUser().getUserId());
        if (tempAuth != null) {
            // 如果绑定过则直接退出, 以保证平台帐号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            // 如果之前没有绑定过平台帐号, 则创建一个平台帐号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            // 对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            // 判断创建是否成功
            if (effectedNum <= 0) {
                throw new LocalAuthOperationException("帐号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalAuthExecution modifyLocalAuth(Integer userId, String userName, String password, String newPassword)
            throws LocalAuthOperationException {
        // 非空判断, 判断传入的用户Id帐号,新旧密码是否为空, 新旧密码是否相同, 若不满足条件则返回错误信息
        if (userId != null && userName != null && password != null && newPassword != null
                && !password.equals(newPassword)) {
            try {
                // 更新密码, 并对新密码进行MD5加密
                int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password),
                        MD5.getMd5(newPassword), new Date());
                // 判断更新是否成功
                if (effectedNum <= 0) {
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new LocalAuthOperationException("更新密码失败:" + e);
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }

}
