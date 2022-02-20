package cn.qingweico.entity;

import java.util.Date;

/**
 * 商品详情图片信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class ProductImage {
    /**
     * 图片ID
     */
    private Long productImageId;
    /**
     * 图片地址
     */
    private String imageAddress;
    /**
     * 图片描述
     */
    private String imageDescription;
    /**
     * 图片权重
     */
    private Integer priority;
    /**
     * 图片创建时间
     */
    private Date createTime;
    /**
     * 图片所属商品ID
     */
    private Long productId;

    public Long getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(Long productImageId) {
        this.productImageId = productImageId;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "productImageId=" + productImageId +
                ", imageAddress='" + imageAddress + '\'' +
                ", imageDescription='" + imageDescription + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", productId=" + productId +
                '}';
    }
}
