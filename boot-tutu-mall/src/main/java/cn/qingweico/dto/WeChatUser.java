package cn.qingweico.dto;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信用户实体类
 *
 * @author zqw
 * @date 2020/10/21
 */
@Data
public class WeChatUser implements Serializable {

    /**
     * openId,标识该公众号下面的该用户的唯一Id
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 用户昵称
     */
    @JsonProperty("nickname")
    private String nickName;
    /**
     * 性别
     */
    @JsonProperty("sex")
    private char sex;
    /**
     * 省份
     */
    @JsonProperty("province")
    private String province;
    /**
     * 城市
     */
    @JsonProperty("city")
    private String city;
    /**
     * 区
     */
    @JsonProperty("country")
    private String country;
    /**
     * 头像图片地址
     */
    @JsonProperty("headimgurl")
    private String headImgUrl;
    /**
     * 语言
     */
    @JsonProperty("language")
    private String language;
    /**
     * 用户权限
     */
    @JsonProperty("privilege")
    private String[] privilege;

    @Override
    public String toString() {
        return "openId:" + this.getOpenId() + ",nickname:" + this.getNickName();
    }
}
