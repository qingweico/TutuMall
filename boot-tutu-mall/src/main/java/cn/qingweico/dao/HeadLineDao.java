package cn.qingweico.dao;

import cn.qingweico.entity.HeadLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/2
 */
@Repository
public interface HeadLineDao {
    /**
     * 根据条件查询头条
     *
     * @param headLineCondition 查询条件 : 头条名称
     * @return 头条列表
     */
    List<HeadLine> queryHeadLine(@Param("condition") HeadLine headLineCondition);

    /**
     * 根据头条id返回唯一的头条信息
     *
     * @param lineId 头条id
     * @return 头条信息
     */
    HeadLine queryHeadLineById(long lineId);

    /**
     * 根据传入的id列表查询头条信息
     *
     * @param lineIdList 头条id集合
     * @return 头条列表
     */
    List<HeadLine> queryHeadLineByIds(List<Long> lineIdList);

    /**
     * 新增头条
     *
     * @param headLine 头条
     * @return effectNum
     */
    int insertHeadLine(HeadLine headLine);

    /**
     * 更新头条信息
     *
     * @param headLine 头条
     * @return effectNum
     */
    int updateHeadLine(HeadLine headLine);

    /**
     * 删除头条
     *
     * @param headLineId 头条id
     * @return effectNum
     */
    int deleteHeadLine(long headLineId);

    /**
     * 批量删除头条
     *
     * @param lineIdList 头条id集合
     * @return effectNum
     */
    int batchDeleteHeadLine(List<Long> lineIdList);
}
