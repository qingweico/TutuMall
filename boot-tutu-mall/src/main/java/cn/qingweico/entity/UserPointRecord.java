package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户积分记录
 *
 * @author zqw
 * @date 2020/10/27
 */
@Data
public class UserPointRecord {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户在该店铺的积分
     */
    private Integer point;
    /**
     * 用户信息(组合查询使用)
     */
    private User user;
    /**
     * 店铺信息
     */
    private Shop shop;
}
