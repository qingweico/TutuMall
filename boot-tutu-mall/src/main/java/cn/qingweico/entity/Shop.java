package cn.qingweico.entity;

import java.util.Date;

/**
 * 店铺信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class Shop {
    /**
     * 店铺ID
     */
    private Integer shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺描述
     */
    private String shopDescription;
    /**
     * 店铺地址
     */
    private String shopAddress;
    /**
     * 店铺的联系方式
     */
    private String phone;
    /**
     * 店铺缩略图地址
     */
    private String shopImage;
    /**
     * 店铺权重
     */
    private Integer priority;
    /**
     * 店铺的创建时间
     */
    private Date createTime;
    /**
     * 店铺的更新时间
     */
    private Date lastEditTime;
    /**
     * 店铺的状态 -1:不可用 0:审核中 1:可用
     */
    private Integer enableStatus;
    /**
     * 店铺所属的地区
     */
    private Area area;
    /**
     * 店铺创建者的信息
     */
    private User user;
    /**
     * 店铺所属的类别
     */
    private ShopCategory shopCategory;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopDescription='" + shopDescription + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", area=" + area +
                ", user=" + user +
                ", shopCategory=" + shopCategory +
                '}';
    }
}
