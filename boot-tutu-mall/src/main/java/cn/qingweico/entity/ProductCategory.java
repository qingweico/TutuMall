package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 商品分类信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class ProductCategory {
    /**
     * 商品类别ID
     */
    private Long id;
    /**
     * 所属的店铺ID
     */
    private Long shopId;
    /**
     * 商品类别名称
     */
    private String name;
    /**
     * 商品权重
     */
    private Integer priority;
    /**
     * 商品类别的创建时间
     */
    private Date createTime;
}
