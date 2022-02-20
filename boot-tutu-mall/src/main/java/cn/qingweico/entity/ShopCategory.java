package cn.qingweico.entity;

import java.util.Date;

/**
 * 店铺分类信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class ShopCategory {
    /**
     * 店铺类别ID
     */
    private Integer shopCategoryId;
    /**
     * 店铺类别名称
     */
    private String shopCategoryName;
    /**
     * 店铺类别描述
     */
    private String shopCategoryDescription;
    /**
     * 店铺类别图片地址
     */
    private String shopCategoryImage;
    /**
     * 店铺类别权重 店铺类别权重越高排名越靠前
     */
    private Integer priority;
    /**
     * 店铺创建时间
     */
    private Date createTime;
    /**
     * 店铺类别信息的更新时间
     */
    private Date lastEditTime;
    /**
     * 所属的店铺类别
     */
    private ShopCategory parent;

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public String getShopCategoryDescription() {
        return shopCategoryDescription;
    }

    public void setShopCategoryDescription(String shopCategoryDescription) {
        this.shopCategoryDescription = shopCategoryDescription;
    }

    public String getShopCategoryImage() {
        return shopCategoryImage;
    }

    public void setShopCategoryImage(String shopCategoryImage) {
        this.shopCategoryImage = shopCategoryImage;
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

    public ShopCategory getParent() {
        return parent;
    }

    public void setParent(ShopCategory parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "ShopCategory{" +
                "shopCategoryId=" + shopCategoryId +
                ", shopCategoryName='" + shopCategoryName + '\'' +
                ", shopCategoryDescription='" + shopCategoryDescription + '\'' +
                ", shopCategoryImage='" + shopCategoryImage + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", parent=" + parent +
                '}';
    }
}
