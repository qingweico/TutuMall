package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺分类信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class ShopCategory {
    /**
     * 店铺类别ID
     */
    private Long id;
    /**
     * 店铺类别名称
     */
    private String name;
    /**
     * 店铺类别描述
     */
    private String desc;
    /**
     * 店铺类别图片地址
     */
    private String imgUrl;
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
     * 所属的父类店铺类别(默认为空)
     */
    private ShopCategory parent;
}
