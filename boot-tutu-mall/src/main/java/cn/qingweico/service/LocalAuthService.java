package cn.qingweico.service;

import cn.qingweico.dto.LocalAuthExecution;
import cn.qingweico.entity.LocalAuth;

/**
 * @author zqw
 * @date 2020/11/11
 */

public interface LocalAuthService {
    /**
     * 通过帐号和密码获取平台帐号信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 本地账号
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

    /**
     * 通过userId获取平台帐号信息
     *
     * @param userId 用户id
     * @return 本地账号
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 绑定微信,生成平台专属的帐号
     *
     * @param localAuth 绑定的本地账号
     * @return LocalAuthExecution
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth);

    /**
     * 修改平台帐号的登录密码
     *
     * @param userId      本地账号id
     * @param username    用户名
     * @param password    密码
     * @param newPassword 新密码
     * @return LocalAuthExecution
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword);
}
