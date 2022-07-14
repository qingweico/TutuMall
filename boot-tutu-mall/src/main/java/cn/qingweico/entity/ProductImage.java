package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 商品详情图片信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class ProductImage {
    /**
     * 图片ID
     */
    private Long id;
    /**
     * 图片地址
     */
    private String imgUrl;
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
}
