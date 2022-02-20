package cn.qingweico.service;

import cn.qingweico.dto.HeadLineExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/15
 */
public interface HeadLineService {
    /**
     * 头条信息缓存的key
     */
    String HEADLINE_LIST_KEY = "headLineList";

    /**
     * 根据传入的条件返回指定的头条列表
     *
     * @param headLineCondition 查询条件
     * @return 头条列表
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition);

    /**
     * 添加头条信息，并存储头条图片
     *
     * @param headLine  头条
     * @param thumbnail 头条缩略图
     * @return HeadLineExecution
     */
    HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail);

    /**
     * 修改头条信息
     *
     * @param headLine  头条
     * @param thumbnail 头条缩略图
     * @return HeadLineExecution
     */
    HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail);

    /**
     * 删除单条头条
     *
     * @param headLineId 头条id
     * @return HeadLineExecution
     */
    HeadLineExecution removeHeadLine(long headLineId);

    /**
     * 批量删除头条
     *
     * @param headLineIdList 头条id集合
     * @return HeadLineExecution
     */
    HeadLineExecution removeHeadLineList(List<Long> headLineIdList);
}
