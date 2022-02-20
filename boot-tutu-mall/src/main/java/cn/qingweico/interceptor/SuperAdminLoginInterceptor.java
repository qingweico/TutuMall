package cn.qingweico.interceptor;

import cn.qingweico.entity.User;
import cn.qingweico.enums.UserTypeEnum;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 周庆伟
 * @date 2020/10/12
 */
@Component
public class SuperAdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        // 从session中获取登录的用户信息
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            User user = (User) userObj;
            // 判断用户是否真的为管理员
            return user.getUserId() != null &&
                    user.getUserId() > 0
                    && UserTypeEnum.ADMIN.getType().equals(user.getUserType());
        }
        return false;
    }
}
