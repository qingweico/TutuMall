package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 商品的日销售量
 *
 * @author zqw
 * @date 2020/10/18
 */
@Data
public class ProductSellDaily {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 哪天的销量, 精确到天
     */
    private Date createTime;
    /**
     * 销量
     */
    private Integer total;
    /**
     * 商品信息实体类
     */
    private Product product;
    /**
     * 店铺信息实体类
     */
    private Shop shop;
}
