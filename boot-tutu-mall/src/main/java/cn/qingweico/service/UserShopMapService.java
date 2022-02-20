package cn.qingweico.service;

import cn.qingweico.dto.UserShopMapExecution;
import cn.qingweico.entity.UserShopMap;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/14
 */
@Repository
public interface UserShopMapService {

    /**
     * 根据传入的查询信息分页查询用户积分列表
     *
     * @param userShopMapCondition 查询条件
     * @param pageIndex            查询起始索引
     * @param pageSize             每页的数量
     * @return UserShopMapExecution
     */
    UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize);

    /**
     * 根据用户Id和店铺Id返回该用户在某个店铺的积分情况
     *
     * @param userId 用户id
     * @param shopId 店铺id
     * @return UserShopMap
     */
    UserShopMap getUserShopMap(long userId, long shopId);

}
