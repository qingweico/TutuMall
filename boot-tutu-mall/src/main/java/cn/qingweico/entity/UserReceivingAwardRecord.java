package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户领取奖品记录
 *
 * @author zqw
 * @date 2020/10/17
 */
@Data
public class UserReceivingAwardRecord {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 使用状态0.未兑换 1.已兑换
     */
    private Integer usedStatus;
    /**
     * 领取奖品所消耗的积分
     */
    private Integer point;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 奖品
     */
    private Award award;
    /**
     * 奖品所属店铺id
     */
    private Long shopId;
    /**
     * 扫码人员id
     */
    private Long operatorId;
}
