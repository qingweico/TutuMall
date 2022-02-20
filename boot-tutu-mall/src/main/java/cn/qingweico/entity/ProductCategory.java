package cn.qingweico.entity;

import java.util.Date;

/**
 * 商品分类信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class ProductCategory {
    /**
     * 商品类别ID
     */
    private Long productCategoryId;
    /**
     * 所属的店铺ID
     */
    private Integer shopId;
    /**
     * 商品类别名称
     */
    private String productCategoryName;
    /**
     * 商品权重
     */
    private Integer priority;
    /**
     * 商品类别的创建时间
     */
    private Date createTime;

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
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

    @Override
    public String toString() {
        return "ProductCategory{" +
                "productCategoryId=" + productCategoryId +
                ", shopId=" + shopId +
                ", productCategoryName='" + productCategoryName + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                '}';
    }
}
