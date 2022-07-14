package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 奖品信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class Award {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 奖品名称
     */
    private String name;
    /**
     * 奖品描述
     */
    private String desc;
    /**
     * 奖品图片地址
     */
    private String imgUrl;
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
    private Long shopId;
}
