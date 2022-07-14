package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺信息
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class Shop {
    /**
     * 店铺ID
     */
    private Long id;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店铺描述
     */
    private String desc;
    /**
     * 店铺地址
     */
    private String address;
    /**
     * 店铺的联系方式
     */
    private String phone;
    /**
     * 店铺缩略图地址
     */
    private String imgUrl;
    /**
     * 店铺权重
     */
    private Integer priority;
    /**
     * 店铺的创建时间
     */
    private Date createTime;
    /**
     * 店铺的更新时间
     */
    private Date lastEditTime;
    /**
     * 店铺的状态 -1:不可用 0:审核中 1:可用
     */
    private Integer enableStatus;
    /**
     * 店铺所属的地区id
     */
    private Long areaId;
    /**
     * 店铺创建者的id
     */
    private Long creatorId;
    /**
     * 店铺所属的类别
     */
    private ShopCategory shopCategory;
}
