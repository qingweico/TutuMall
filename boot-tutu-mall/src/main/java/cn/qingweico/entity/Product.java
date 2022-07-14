package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class Product {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品描述
     */
    private String desc;
    /**
     * 商品缩略图地址
     */
    private String imgUrl;
    /**
     * 商品原价
     */
    private String normalPrice;
    /**
     * 商品折扣价
     */
    private String promotionPrice;
    /**
     * 商品权重
     */
    private Integer priority;
    /**
     * 商品积分
     */
    private Integer point;
    /**
     * 商品的创建时间
     */
    private Date createTime;
    /**
     * 商品的更新时间
     */
    private Date lastEditTime;
    /**
     * 商品状态 0:下架 1:展示
     */
    private Integer enableStatus;
    /**
     * 商品详情图片列表
     */
    private List<ProductImage> productImageList;
    /**
     * 所属商品分类
     */
    private ProductCategory productCategory;
    /**
     * 所属店铺id
     */
    private Long shopId;
}
