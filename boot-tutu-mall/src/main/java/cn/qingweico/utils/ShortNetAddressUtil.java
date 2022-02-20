package cn.qingweico.utils;

import net.sf.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 使用微信公众号
 * 短链接的生成(2021/3/15以后停止该接口新生成的短链)
 * @author 周庆伟
 * @date 2021/2/25
 */

@Component
@ConfigurationProperties(prefix = "wechat")
public class ShortNetAddressUtil {

    private static String appId;

    private static String appSecret;

    public void setAppId(String appId) {
        ShortNetAddressUtil.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        ShortNetAddressUtil.appSecret = appSecret;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getToken() {
        // 获取access_token的get请求地址
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APP_ID&secret=APP_SECRET";
        String requestUrl = tokenUrl.replace("APP_ID", appId).replace("APP_SECRET", appSecret);
        String string = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        return (String) JSONObject.fromObject(string).get("access_token");
    }

    public static String getShortAddress(String longUrl) {
        String token = getToken();
        String sUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        sUrl = sUrl.replace("ACCESS_TOKEN", token);
        String param = "{\"action\":\"long2short\","
                + "\"long_url\":\"" + longUrl + "\"}";
        String jsonString = WeChatUtil.httpsRequest(sUrl, "POST", param);
        return (String) JSONObject.fromObject(jsonString).get("short_url");
    }
}
