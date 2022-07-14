package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 微信账号授权记录
 *
 * @author zqw
 * @date 2020/11/05
 */
@Data
public class WeChatAuthRecord {
    /**
     * 微信账号ID
     */
    private Long id;
    /**
     * openId
     */
    private String openId;
    /**
     * 微信账号绑定平台时间
     */
    private Date createTime;
    /**
     * 微信账号绑定的用户
     */
    private User user;
}
