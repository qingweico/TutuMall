package cn.qingweico.utils;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zqw
 * @date 2020/9/15
 */
public class HttpServletRequestUtil {
    public static int getInteger(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }
    }

    public static Boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if (result != null) {
                result = result.trim();
            }
            if (StringUtils.EMPTY.equals(result)) {
                return null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查是否需要验证码
     * @param request HttpServletRequest
     * @return statusChange true or false
     */
    public static boolean checkStatusChange(HttpServletRequest request) {
        return HttpServletRequestUtil.getBoolean(request, "statusChange");
    }
}
