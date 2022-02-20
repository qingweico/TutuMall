package cn.qingweico.entity;

import java.util.Date;

/**
 * 首页头条信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */

public class HeadLine {
    /**
     * 头条ID
     */
    private Long lineId;
    /**
     * 头条链接
     */
    private String lineLink;
    /**
     * 头条名称
     */
    private String lineName;
    /**
     * 头条图片
     */
    private String lineImage;
    /**
     * 头条权重 头条按照权重大小展示
     */
    private Integer priority;
    /**
     * 头条状态
     */
    private Integer enableStatus;
    /**
     * 头条的创建时间
     */
    private Date createTime;
    /**
     * 头条的更新时间
     */
    private Date lastEditTime;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLineLink() {
        return lineLink;
    }

    public void setLineLink(String lineLink) {
        this.lineLink = lineLink;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineImage() {
        return lineImage;
    }

    public void setLineImage(String lineImg) {
        this.lineImage = lineImg;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
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
        return "HeadLine{" +
                "lineId=" + lineId +
                ", lineLink='" + lineLink + '\'' +
                ", lineName='" + lineName + '\'' +
                ", lineImage='" + lineImage + '\'' +
                ", priority=" + priority +
                ", enableStatus=" + enableStatus +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                '}';
    }
}
