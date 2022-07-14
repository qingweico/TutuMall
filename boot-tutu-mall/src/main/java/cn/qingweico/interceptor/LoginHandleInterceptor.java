package cn.qingweico.interceptor;

import cn.qingweico.entity.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zqw
 * @date 2020/10/16
 */
@Component
public class LoginHandleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        Object args = request.getSession().getAttribute("user");
        if (args != null) {
            User user = (User) args;
            if (user.getId() != null && user.getEnableStatus() == 1) {
                return true;
            }
        }
        response.sendRedirect("/local/login");
        return false;
    }
}
