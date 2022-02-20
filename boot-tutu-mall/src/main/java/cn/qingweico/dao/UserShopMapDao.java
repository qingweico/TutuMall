package cn.qingweico.dao;


import java.util.List;

import cn.qingweico.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/3
 */
@Repository
public interface UserShopMapDao {
    /**
     * 根据查询条件分页返回用户店铺积分列表
     *
     * @param userShopCondition 查询条件
     * @param rowIndex          查询起始条件
     * @param pageSize          每页的数量
     * @return 店铺积分列表
     */
    List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition,
                                           @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合queryUserShopMapList根据相同的查询条件返回用户店铺积分记录总数
     *
     * @param userShopCondition 查询条件
     * @return 用户店铺积分记录总数
     */
    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

    /**
     * 根据传入的用户Id和shopId查询该用户在某个店铺的积分信息
     *
     * @param userId 用户di
     * @param shopId 店铺id
     * @return UserShopMap
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);

    /**
     * 添加一条用户店铺的积分记录
     *
     * @param userShopMap UserShopMap
     * @return effectNum
     */
    int insertUserShopMap(UserShopMap userShopMap);

    /**
     * 更新用户在某店铺的积分
     *
     * @param userShopMap UserShopMap
     * @return effectNum
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);

}
