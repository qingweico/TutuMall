package cn.qingweico.entity;

import java.util.Date;

/**
 * 微信账号信息
 *
 * @author 周庆伟
 * @date 2020/11/05
 */
public class WeChatAuth {
    /**
     * 微信账号ID
     */
    private Long weChatAuthId;
    /**
     * openId
     */
    private String openId;
    /**
     * 微信账号绑定平台时间
     */
    private Date createTime;
    /**
     * 微信账号绑定的用户信息
     */
    private User user;

    public Long getWeChatAuthId() {
        return weChatAuthId;
    }

    public void setWeChatAuthId(Long weChatAuthId) {
        this.weChatAuthId = weChatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WeChatAuth{" +
                "weChatAuthId=" + weChatAuthId +
                ", openId='" + openId + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                '}';
    }
}
