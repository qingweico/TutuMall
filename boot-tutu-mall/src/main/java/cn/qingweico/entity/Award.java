package cn.qingweico.entity;

import java.util.Date;

/**
 * 奖品信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */
public class Award {
    /**
     * 主键Id
     */
    private Long awardId;
    /**
     * 奖品名称
     */
    private String awardName;
    /**
     * 奖品描述
     */
    private String awardDescription;
    /**
     * 奖品图片地址
     */
    private String awardImage;
    /**
     * 需要多少积分去兑换
     */
    private Integer point;
    /**
     * 权重 越大越排前显示
     */
    private Integer priority;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最近一次的更新时间
     */
    private Date lastEditTime;
    /**
     * 可用状态 0.不可用 1.可用
     */
    private Integer enableStatus;
    /**
     * 所属店铺id
     */
    private Integer shopId;

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getAwardDescription() {
        return awardDescription;
    }

    public void setAwardDescription(String awardDescription) {
        this.awardDescription = awardDescription;
    }

    public String getAwardImage() {
        return awardImage;
    }

    public void setAwardImage(String awardImage) {
        this.awardImage = awardImage;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "Award{" +
                "awardId=" + awardId +
                ", awardName='" + awardName + '\'' +
                ", awardDescription='" + awardDescription + '\'' +
                ", awardImage='" + awardImage + '\'' +
                ", point=" + point +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", shopId=" + shopId +
                '}';
    }
}
