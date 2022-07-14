package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 本地账号信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class LocalAuth {
    /**
     * 本地账号ID
     */
    private Long id;
    /**
     * 本地账号用户名
     */
    private String username;
    /**
     * 本地账号密码
     */
    private String password;
    /**
     * 本地账户的创建时间
     */
    private Date createTime;
    /**
     * 本地账号的更新时间
     */
    private Date lastEditTime;
    /**
     * 本地账号绑定的用户信息
     */
    private User user;
}
