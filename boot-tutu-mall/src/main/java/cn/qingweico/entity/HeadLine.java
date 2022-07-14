package cn.qingweico.entity;

import lombok.Data;

import java.util.Date;

/**
 * 首页头条信息
 *
 * @author zqw
 * @date 2020/09/05
 */
@Data
public class HeadLine {
    /**
     * 头条ID
     */
    private Long id;
    /**
     * 头条链接
     */
    private String link;
    /**
     * 头条名称
     */
    private String name;
    /**
     * 头条图片
     */
    private String imgUrl;
    /**
     * 头条权重 头条按照权重大小展示
     */
    private Integer priority;
    /**
     * 头条状态
     */
    private Integer enableStatus;
    /**
     * 头条的创建时间
     */
    private Date createTime;
    /**
     * 头条的更新时间
     */
    private Date lastEditTime;
}
