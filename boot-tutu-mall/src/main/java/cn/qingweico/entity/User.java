package cn.qingweico.entity;

import java.util.Date;

/**
 * 用户信息
 *
 * @author 周庆伟
 * @date 2020/10/24
 */

public class User {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户头像地址
     */
    private String profileImage;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户性别
     */
    private char gender;
    /**
     * 用户状态 -1:不可用 0:审核中 1:可用
     */
    private Integer enableStatus;
    /**
     * 用户身份标识 1:顾客 2:店家 3:管理员
     */
    private Integer userType;
    /**
     * 用户信息的创建时间
     */
    private Date createTime;
    /**
     * 用户信息的更新时间
     */
    private Date lastEditTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", enableStatus=" + enableStatus +
                ", userType=" + userType +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                '}';
    }
}
