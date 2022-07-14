package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺授权记录
 *
 * @author zqw
 * @date 2020/10/19
 */
@Data
public class ShopAuthRecord {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 授权名称
     */
    private String title;
    /**
     * 职称
     */
    private Integer titleFlag;
    /**
     * 授权有效状态,0.无效 1.有效
     */
    private Integer enableStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最近一次的更新时间
     */
    private Date lastEditTime;
    /**
     * 授权人员id
     */
    private Long userId;
    /**
     * 店铺id
     */
    private Long shopId;
}
