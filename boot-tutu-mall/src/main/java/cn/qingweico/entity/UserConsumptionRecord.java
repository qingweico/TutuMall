package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;


/**
 * 用户消费记录
 *
 * @author zqw
 * @date 2020/10/29
 */
@Data
public class UserConsumptionRecord {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 消费商品所获得的积分
     */
    private Integer point;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品信息实体类
     */
    private Product product;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 扫码员id
     */
    private Long operatorId;
}
