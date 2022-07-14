package cn.qingweico.service.impl;

import java.util.Date;

import cn.qingweico.dao.LocalAuthDao;
import cn.qingweico.dto.LocalAuthExecution;
import cn.qingweico.entity.LocalAuth;
import cn.qingweico.enums.LocalAuthStateEnum;
import cn.qingweico.service.LocalAuthService;
import cn.qingweico.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/11/11
 */

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Resource
    private LocalAuthDao localAuthDao;


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
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
        // 查询此用户是否已绑定过平台帐号
        LocalAuth la = localAuthDao.queryLocalByUserId(localAuth.getId());
        if (la != null) {
            // 如果绑定过则直接退出, 以保证平台帐号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        // 如果之前没有绑定过平台帐号, 则创建一个平台帐号与该用户绑定
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        // 对密码进行MD5加密
        localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
        // 判断创建是否成功
        if (localAuthDao.insertLocalAuth(localAuth) <= 0) {
            return new LocalAuthExecution(LocalAuthStateEnum.INNER_ERROR);
        }
        return new LocalAuthExecution(LocalAuthStateEnum.BIND_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
        // 新旧密码判断
        if (!password.equals(newPassword)) {
            // 更新密码, 并对新密码进行MD5加密
            int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),
                    MD5.getMd5(newPassword), new Date());
            // 判断更新是否成功
            if (effectedNum <= 0) {
                return new LocalAuthExecution(LocalAuthStateEnum.INNER_ERROR);
            }
            return new LocalAuthExecution(LocalAuthStateEnum.PASSWORD_SUCCESS);
        }
        return new LocalAuthExecution(LocalAuthStateEnum.PASSWORD_DIFFERENT);
    }
}
