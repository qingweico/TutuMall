package cn.qingweico.entity;

/**
 * 用来接收平台二维码的信息
 *
 * @author 周庆伟
 * @date 2020/11/11
 */
public class WeChatInfo {

    private Integer customerId;

    private Long productId;

    private Long userAwardId;

    private Long createTime;

    private Integer shopId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Long userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WeChatInfo{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                ", userAwardId=" + userAwardId +
                ", createTime=" + createTime +
                ", shopId=" + shopId +
                '}';
    }
}
