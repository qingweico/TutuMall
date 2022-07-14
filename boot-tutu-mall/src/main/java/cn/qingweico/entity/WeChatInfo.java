package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用来接收平台二维码的信息
 *
 * @author zqw
 * @date 2020/11/11
 */
@Data
public class WeChatInfo {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 消费的用户id
     */
    private Long customerId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 若为兑换商品操作,则为用户奖品id,否则为空
     */
    private Long userAwardId;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 店铺id
     */
    private Long shopId;
}
