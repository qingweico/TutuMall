package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @author zqw
 * @date 2020/10/24
 */
@Data
public class User {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户头像地址
     */
    private String avatar;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户性别
     */
    private char gender;
    /**
     * 用户状态 -1:不可用 0:审核中 1:可用
     */
    private Integer enableStatus;
    /**
     * 用户身份标识 1:顾客 2:店家 3:管理员
     */
    private Integer userType;
    /**
     * 用户信息的创建时间
     */
    private Date createTime;
    /**
     * 用户信息的更新时间
     */
    private Date lastEditTime;
}
