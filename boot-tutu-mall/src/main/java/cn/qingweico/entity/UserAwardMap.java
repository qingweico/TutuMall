package cn.qingweico.entity;

import java.util.Date;

/**
 * 顾客与其兑换奖品之间的映射
 *
 * @author 周庆伟
 * @date 2020/10/17
 */

public class UserAwardMap {
    /**
     * 主键Id
     */
    private Long userAwardId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 使用状态0.未兑换 1.已兑换
     */
    private Integer usedStatus;
    /**
     * 领取奖品所消耗的积分
     */
    private Integer point;
    /**
     * 顾客信息实体类
     */
    private User user;
    /**
     * 奖品信息实体类
     */
    private Award award;
    /**
     * 店铺信息实体类
     */
    private Shop shop;
    /**
     * 操作员信息实体类
     */
    private User operator;

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Long userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "UserAwardMap{" +
                "userAwardId=" + userAwardId +
                ", createTime=" + createTime +
                ", usedStatus=" + usedStatus +
                ", point=" + point +
                ", user=" + user +
                ", award=" + award +
                ", shop=" + shop +
                ", operator=" + operator +
                '}';
    }
}
