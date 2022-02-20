package cn.qingweico.dto;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信用户实体类
 *
 * @author 周庆伟
 * @date 2020/10/21
 */
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "openId:" + this.getOpenId() + ",nickname:" + this.getNickName();
    }
}
