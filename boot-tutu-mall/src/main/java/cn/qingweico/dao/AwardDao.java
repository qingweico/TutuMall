package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.Award;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/2
 */

@Repository
public interface AwardDao {
    /**
     * 依据传入进来的查询条件分页显示奖品信息列表
     *
     * @param awardCondition 查询条件
     * @param rowIndex       开始查询下标
     * @param pageSize       每页数量
     * @return 奖品信息列表
     */
    List<Award> queryAwardList(@Param("awardCondition") Award awardCondition,
                               @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    /**
     * 配合queryAwardList返回相同查询条件下的奖品数
     *
     * @param awardCondition 查询条件
     * @return 奖品数
     */
    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    /**
     * 通过awardId查询奖品信息
     *
     * @param awardId 奖品id
     * @return 奖品信息
     */
    Award queryAwardByAwardId(long awardId);

    /**
     * 添加奖品信息
     *
     * @param award 待添加的奖品
     * @return effectNum
     */
    int insertAward(Award award);

    /**
     * 更新奖品信息
     *
     * @param award 待更新的奖品
     * @return effectNum
     */
    int updateAward(Award award);

    /**
     * 删除奖品信息
     *
     * @param awardId 待删除的奖品id
     * @param shopId  该奖品所属的店铺id
     * @return effectNum
     */
    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
}
