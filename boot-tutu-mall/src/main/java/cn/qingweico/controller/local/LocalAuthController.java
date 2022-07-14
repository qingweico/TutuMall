package cn.qingweico.controller.local;


import cn.qingweico.common.Result;
import cn.qingweico.dto.LocalAuthExecution;
import cn.qingweico.entity.LocalAuth;
import cn.qingweico.entity.User;
import cn.qingweico.enums.LocalAuthStateEnum;
import cn.qingweico.service.LocalAuthService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * -------------- 本地账号登录 --------------
 * @author zqw
 * @date 2020/11/11
 */
@Slf4j
@RestController
@RequestMapping("local")
public class LocalAuthController {

    @Resource
    private LocalAuthService localAuthService;


    /**
     * 检查验证码
     *
     * @param request HttpServletRequest
     * @return false or true
     */
    private boolean isCheckVerificationCode(HttpServletRequest request) {
        return !CodeUtil.checkVerificationCode(request);
    }

    /**
     * 将用户信息与平台帐号绑定
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/bind")
    private Result bind(HttpServletRequest request) {
        if (isCheckVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        // 获取输入的帐号
        String userName = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 从session中获取当前用户信息
        User user = (User) request.getSession().getAttribute("user");
        // 非空判断, 要求帐号密码以及当前的用户session非空
        if (userName != null &&
                password != null &&
                user != null &&
                user.getId() != null) {
            // 创建LocalAuth对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(userName);
            localAuth.setPassword(password);
            localAuth.setUser(user);
            // 绑定帐号
            LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                return Result.ok(user.getUserType());
            } else {
                return Result.errorMsg(le.getStateInfo());
            }
        }
        log.info("username: {}, password: {}, user: {}", userName, password, user);
        return Result.errorCustom(ResponseStatusEnum.AUTH_FAIL);
    }

    /**
     * 修改密码
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/changePwd")
    private Result changePwd(HttpServletRequest request) {
        if (isCheckVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        // 获取帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取原密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 获取新密码
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        // 从session中获取当前用户信息(用户一旦通过微信登录之后, 便能获取到用户的信息)
        User user = (User) request.getSession().getAttribute("user");
        // 非空判断, 要求帐号新旧密码以及当前的用户session非空, 且新旧密码不可以相同
        if (username != null
                && password != null
                && newPassword != null
                && user != null
                && user.getId() != null
                && !password.equals(newPassword)) {
            // 查看原先帐号, 看看与输入的帐号是否一致, 不一致则认为是非法操作
            LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getId());
            if (localAuth == null || !localAuth.getUsername().equals(username)) {
                // 不一致则直接退出
                return Result.errorCustom(ResponseStatusEnum.DIFFERENT_ACCOUNT);
            }
            // 修改平台帐号的用户密码
            LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getId(), username, password,
                    newPassword);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                return Result.ok(user.getUserType());
            } else {
                return Result.errorMsg(le.getStateInfo());
            }

        }
        log.info("username: {}, password: {}, newPassword: {}, user: {}", username, password, newPassword, user);
        return Result.error();
    }

    /**
     * 用户登陆
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/loginCheck")
    private Result loginCheck(HttpServletRequest request) {
        // 获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && isCheckVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        // 获取输入的帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 非空校验
        if (username != null && password != null) {
            // 传入帐号和密码去获取平台帐号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
            if (localAuth != null) {
                if (localAuth.getUser().getEnableStatus() == 1) {
                    // 若能取到帐号信息且可用状态不为0则登录成功 同时在session里设置用户信息
                    request.getSession().setAttribute("user", localAuth.getUser());
                    return Result.ok(localAuth.getUser().getUserType());
                } else {
                    return Result.errorCustom(ResponseStatusEnum.FORBIDDEN_ACCOUNT);
                }
            } else {
                // 用户名或者密码错误
                return Result.errorCustom(ResponseStatusEnum.AUTH_FAIL);
            }
        } else {
            // 用户名或者密码为空
            return Result.errorCustom(ResponseStatusEnum.AUTH_INFO_NULL);
        }
    }

    /**
     * 用户点击登出按钮的时候注销session
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/logout")
    private Result logout(HttpServletRequest request) {
        // 将用户session置为空
        request.getSession().setAttribute("user", null);
        return Result.ok();
    }

    /**
     * 检查用户是否登陆
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/checkLogin")
    private Result checkLogin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return Result.ok(user != null);
    }
}
