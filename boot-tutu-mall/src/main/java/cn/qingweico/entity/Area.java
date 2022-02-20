package cn.qingweico.entity;

import java.util.Date;

/**
 * 地区信息
 *
 * @author 周庆伟
 * @date 2020/09/05
 */
public class Area {
    /**
     * 地区ID
     */

    private Integer areaId;
    /**
     * 地区名称
     */

    private String areaName;
    /**
     * 地区权重 权重越高的展示在最前面
     */
    private Integer priority;
    /**
     * 地区的创建时间
     */
    private Date createTime;
    /**
     * 地区信息的更新时间
     */
    private Date lastEditTime;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    @Override
    public String toString() {
        return "Area{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                '}';
    }
}
