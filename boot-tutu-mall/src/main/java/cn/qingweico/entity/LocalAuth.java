package cn.qingweico.entity;

import java.util.Date;

/**
 * 本地账号信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class LocalAuth {
    /**
     * 本地账号ID
     */
    private Long localAuthId;
    /**
     * 本地账号用户名
     */
    private String username;
    /**
     * 本地账号密码
     */
    private String password;
    /**
     * 本地账户的创建时间
     */
    private Date createTime;
    /**
     * 本地账号的更新时间
     */
    private Date lastEditTime;
    /**
     * 本地账号绑定的用户信息
     */
    private User user;

    public Long getLocalAuthId() {
        return localAuthId;
    }

    public void setLocalAuthId(Long localAuthId) {
        this.localAuthId = localAuthId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LocalAuth{" +
                "localAuthId=" + localAuthId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", user=" + user +
                '}';
    }
}
