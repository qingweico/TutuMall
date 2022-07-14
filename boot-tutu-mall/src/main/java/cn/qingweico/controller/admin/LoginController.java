package cn.qingweico.controller.admin;

import cn.qingweico.common.Constants;
import cn.qingweico.common.Result;
import cn.qingweico.entity.LocalAuth;
import cn.qingweico.entity.UserInfo;
import cn.qingweico.service.LocalAuthService;
import cn.qingweico.utils.JwtUtils;
import cn.qingweico.utils.ResponseStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author zqw
 * @date 2020/11/14
 */

@RestControllerAdvice
@RequestMapping("/a")
public class LoginController {
    @Resource
    private LocalAuthService localAuthService;

    /**
     * 登录验证
     *
     * @return Map
     */
    @GetMapping("/login")
    private Result loginCheck(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        // 空值判断
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            // 获取本地帐号授权信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
            if (localAuth != null) {
                // 若帐号密码正确, 则验证用户的身份是否为超级管理员
                if (localAuth.getUser().getUserType().equals(Constants.USER_TYPE_ADMIN)) {
                    String token = JwtUtils.createToken(localAuth.getUsername());
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUsername(localAuth.getUsername());
                    userInfo.setToken(token);
                    return new Result(ResponseStatusEnum.LOGIN_SUCCESS, userInfo);
                } else {
                    return new Result(ResponseStatusEnum.UN_AUTH);
                }
            } else {
                return Result.errorCustom(ResponseStatusEnum.AUTH_FAIL);
            }
        } else {
            return Result.errorCustom(ResponseStatusEnum.AUTH_INFO_NULL);
        }
    }

    @GetMapping("checkToken")
    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            return false;
        }
        try {
            JwtUtils.parse(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
