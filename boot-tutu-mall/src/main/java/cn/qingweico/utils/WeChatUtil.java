package cn.qingweico.utils;


import cn.qingweico.dto.UserAccessToken;
import cn.qingweico.dto.WeChatUser;
import cn.qingweico.entity.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 微信工具类
 *
 * @author 周庆伟
 * @date 2020/10/21
 */
@Slf4j
@Component
public class WeChatUtil {
    
    private static final String REQUEST_TYPE_GET = "GET";

    private static String APP_ID;

    private static String APP_SECRET;

    @Value("${wechat.appid}")
    public void setAppId(String appId) {
        APP_ID = appId;
    }

    @Value("${wechat.appsecret}")
    public void setAppSecret(String appSecret) {
        APP_SECRET = appSecret;
    }

    /**
     * 获取UserAccessToken实体类
     *
     * @param code code
     * @return UserAccessToken
     */
    public static UserAccessToken getUserAccessToken(String code) {
        // 测试号信息里的appId
        log.debug("appId:" + APP_ID);
        // 测试号信息里的appSecret
        log.debug("secret:" + APP_SECRET);
        // 根据传入的code,拼接出访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                APP_ID + "&secret=" + APP_SECRET
                + "&code=" + code + "&grant_type=authorization_code";
        // 向相应URL发送请求获取token json字符串
        String tokenString = httpsRequest(url, "GET", null);
        log.debug("userAccessToken:" + tokenString);
        UserAccessToken token = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成相应对象
            token = objectMapper.readValue(tokenString, UserAccessToken.class);
        } catch (JsonParseException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (token == null) {
            log.error("获取用户accessToken失败");
            return null;
        }
        return token;
    }

    /**
     * 获取WeChatUser实体类
     *
     * @param accessToken accessToken
     * @param openId      openId
     * @return WeChatUser
     */
    public static WeChatUser getUserInfo(String accessToken, String openId) {
        // 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        // 访问该URL获取用户信息json 字符串
        String userStr = httpsRequest(url, "GET", null);
        log.debug("user info :" + userStr);
        WeChatUser user = new WeChatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成相应对象
            user = objectMapper.readValue(userStr, WeChatUser.class);
        } catch (JsonParseException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (user == null) {
            log.error("获取用户信息失败!");
            return null;
        }
        return user;
    }

    /**
     * 将WeChatUser里的信息转换成User的信息并返回User实体类
     *
     * @param weChatUser WeChatUser
     * @return User
     */
    public static User getUserFromRequest(WeChatUser weChatUser) {
        if (weChatUser == null) {
            return null;
        }
        User user = new User();
        user.setName(weChatUser.getNickName());
        user.setGender(weChatUser.getSex());
        user.setAvatar(weChatUser.getHeadImgUrl());
        user.setEnableStatus(1);
        return user;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return json字符串
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuilder buffer = new StringBuilder();
        try {
            // 创建SSLContext对象, 并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if (REQUEST_TYPE_GET.equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (outputStr != null) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 防止中文乱码
                outputStream.write(outputStr.getBytes(StandardCharsets.UTF_8));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                buffer.append(s);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
            log.debug("https buffer:" + buffer);
        } catch (ConnectException ce) {
            log.error("WeChat server connection timed out.");
        } catch (Exception e) {
            log.error("https request error :{}", e.getMessage());
        }
        return buffer.toString();
    }
}
