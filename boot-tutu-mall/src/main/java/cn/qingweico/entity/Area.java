package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 地区信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class Area {
    /**
     * 地区ID
     */
    private Long id;
    /**
     * 地区名称
     */
    private String name;
    /**
     * 地区权重;权重越高的展示在最前面
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
}
