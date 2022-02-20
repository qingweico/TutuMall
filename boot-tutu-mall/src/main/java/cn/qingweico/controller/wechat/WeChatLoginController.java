package cn.qingweico.controller.wechat;


import cn.qingweico.dto.UserAccessToken;
import cn.qingweico.dto.WeChatAuthExecution;
import cn.qingweico.dto.WeChatUser;
import cn.qingweico.entity.User;
import cn.qingweico.entity.WeChatAuth;
import cn.qingweico.enums.UserTypeEnum;
import cn.qingweico.enums.WeChatAuthStateEnum;
import cn.qingweico.service.UserService;
import cn.qingweico.service.WeChatAuthService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取关注公众号之后的微信用户信息的接口, 如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdb083dcc9aa17973&redirect_uri
 * =http://mall.qingweico.cn/wechatlogin/logincheck&response_type=code&scope=
 * snsapi_userinfo&state=1&wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 * @author 周庆伟
 * @date 2020/10/21
 */
@Slf4j
@Controller
@RequestMapping("wechat")
public class WeChatLoginController {
    @Resource
    UserService userService;
    @Resource
    private WeChatAuthService wechatAuthService;


    @GetMapping("/loginCheck")
    public String doGet(HttpServletRequest request) {
        log.debug("WeChat login get...");
        // 获取微信公众号传输过来的code, 通过code可获取access_token, 进而获取用户信息
        String code = request.getParameter("code");
        // state可以用来传自定义的信息, 方便程序调用
        Integer roleType = HttpServletRequestUtil.getInteger(request, "state");
        log.debug("WeChat login code: " + code);
        WeChatUser user = null;
        String openId = null;
        WeChatAuth auth = null;
        if (code != null) {
            UserAccessToken token;
            // 通过code获取access_token
            token = WeChatUtil.getUserAccessToken(code);
            Objects.requireNonNull(token);
            log.debug("WeChat login token:" + token);
            // 通过token获取accessToken
            String accessToken = token.getAccessToken();
            // 通过token获取openId
            openId = token.getOpenId();
            // 通过access_token和openId获取用户昵称等信息
            user = WeChatUtil.getUserInfo(accessToken, openId);
            if (user == null) {
                log.error("user为空!");
                throw new RuntimeException("user为空!");
            }
            log.debug("WeChat login user: " + user);
            request.getSession().setAttribute("openId", openId);
            auth = wechatAuthService.getWeChatAuthByOpenId(openId);
        }
        // 若微信帐号为空则需要注册微信帐号, 同时注册用户信息
        if (auth == null) {
            User userInfo = WeChatUtil.getUserFromRequest(user);
            auth = new WeChatAuth();
            auth.setOpenId(openId);
            if (userInfo != null) {
                // 普通用户绑定账号
                if (UserTypeEnum.CUSTOMER.getType().equals(roleType)) {
                    userInfo.setUserType(UserTypeEnum.CUSTOMER.getType());
                } else {
                    // 店家绑定微信账号
                    userInfo.setUserType(UserTypeEnum.OWNER.getType());
                }
            }
            auth.setUser(userInfo);
            WeChatAuthExecution weChatAuthExecution = wechatAuthService.register(auth);
            if (weChatAuthExecution.getState() != WeChatAuthStateEnum.SUCCESS.getState()) {
                return null;
            } else {
                userInfo = userService.getUserById(auth.getUser().getUserId());
                request.getSession().setAttribute("user", userInfo);
            }
        } else {
            request.getSession().setAttribute("user", auth.getUser());
        }
        if (UserTypeEnum.CUSTOMER.getType().equals(roleType)) {
            return "frontend/index";
        } else {
            return "shop/shoplist";
        }
    }
}
