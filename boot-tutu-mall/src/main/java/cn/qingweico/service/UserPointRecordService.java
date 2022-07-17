package cn.qingweico.service;

import cn.qingweico.dto.UserPointRecordExecution;
import cn.qingweico.entity.UserPointRecord;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/14
 */
@Repository
public interface UserPointRecordService {

    /**
     * 根据传入的查询信息分页查询用户积分列表
     *
     * @param condition 查询条件
     * @param page                 查询起始索引
     * @param pageSize             每页的数量
     * @return UserShopMapExecution
     */
    UserPointRecordExecution userPointRecordList(UserPointRecord condition, int page, int pageSize);

    /**
     * 根据用户id和店铺id返回该用户在某个店铺的积分情况
     *
     * @param userId 用户id
     * @param shopId 店铺id
     * @return UserShopMap
     */
    UserPointRecord getUserPointRecord(long userId, long shopId);

}
