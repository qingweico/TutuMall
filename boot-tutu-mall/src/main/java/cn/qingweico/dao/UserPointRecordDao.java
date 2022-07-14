package cn.qingweico.dao;


import java.util.List;

import cn.qingweico.entity.UserPointRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/3
 */
@Repository
public interface UserPointRecordDao {
    /**
     * 根据查询条件分页返回用户在每个店铺下的积分情况
     *
     * @param userPointRecord 查询条件
     * @param rowIndex        查询起始条件
     * @param pageSize        每页的数量
     * @return 店铺积分列表
     */
    List<UserPointRecord> queryUserShopPointList(@Param("userPointRecord") UserPointRecord userPointRecord,
                                                 @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入的用户id和shopId查询该用户在某个店铺的积分信息
     *
     * @param userId 用户di
     * @param shopId 店铺id
     * @return UserShopMap
     */
    UserPointRecord queryUserPointRecord(@Param("userId") long userId, @Param("shopId") long shopId);

    /**
     * 添加一条用户积分记录
     *
     * @param userPointRecord UserPointRecord
     * @return effectNum
     */
    int insertUserPointRecord(UserPointRecord userPointRecord);

    /**
     * 更新用户在某店铺的积分
     *
     * @param userPointRecord UserPointRecord
     * @return effectNum
     */
    int updateUserPointRecord(UserPointRecord userPointRecord);

}
