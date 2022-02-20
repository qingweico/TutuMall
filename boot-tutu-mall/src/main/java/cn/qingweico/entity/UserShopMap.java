package cn.qingweico.entity;

import java.util.Date;

/**
 * 顾客与其所在店铺积分之间的映射关系
 *
 * @author 周庆伟
 * @date 2020/10/27
 */

public class UserShopMap {
    /**
     * 主键Id
     */
    private Long userShopId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 顾客在该店铺的积分
     */
    private Integer point;
    /**
     * 顾客信息实体类
     */
    private User user;
    /**
     * 店铺信息实体类
     */
    private Shop shop;

    public Long getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(Long userShopId) {
        this.userShopId = userShopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "UserShopMap{" +
                "userShopId=" + userShopId +
                ", createTime=" + createTime +
                ", point=" + point +
                ", user=" + user +
                ", shop=" + shop +
                '}';
    }
}
