package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/2
 */
@Repository
public interface UserAwardMapDao {
    /**
     * 根据传入进来的查询条件分页返回用户兑换奖品记录的列表信息
     *
     * @param userAwardCondition 查询条件
     * @param rowIndex           查询起始索引
     * @param pageSize           每页的数量
     * @return 用户兑换奖品记录的列表信息
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入进来的查询条件分页返回用户兑换奖品记录的列表信息
     *
     * @param userAwardCondition 查询条件
     * @param rowIndex           查询起始索引
     * @param pageSize           每页的数量
     * @return 用户兑换奖品记录的列表信息
     */
    List<UserAwardMap> queryReceivedUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
                                                     @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    /**
     * 配合queryUserAwardMapList返回相同查询条件下的兑换奖品记录数
     *
     * @param userAwardCondition 查询条件
     * @return 兑换奖品记录数
     */
    int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

    /**
     * 根据userAwardId返回某条奖品兑换信息
     *
     * @param userAwardId userAwardId
     * @return 奖品兑换信息
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * 添加一条奖品兑换信息
     *
     * @param userAwardMap 奖品兑换信息
     * @return effectNum
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 更新奖品兑换信息,主要更新奖品领取状态
     *
     * @param userAwardMap 奖品兑换信息
     * @return effectNum
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);
}
