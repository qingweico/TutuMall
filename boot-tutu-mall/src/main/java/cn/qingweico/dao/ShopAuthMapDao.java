package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/3
 */

@Repository
public interface ShopAuthMapDao {
    /**
     * 分页列出店铺下面的授权信息
     *
     * @param shopId   店铺id
     * @param rowIndex 起始索引
     * @param pageSize 每页数量
     * @return 店铺授权列表
     */
    List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
                                                   @Param("pageSize") int pageSize);

    /**
     * 获取授权总数
     *
     * @param shopId 店铺id
     * @return 授权总数
     */
    int queryShopAuthCountByShopId(@Param("shopId") long shopId);

    /**
     * 新增一条店铺与店员的授权关系
     *
     * @param shopAuthMap shopAuthMap
     * @return effectedNum
     */
    int insertShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 更新授权信息
     *
     * @param shopAuthMap shopAuthMap
     * @return effectedNum
     */
    int updateShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 免除某员工的权限
     *
     * @param shopAuthId 已授权的员工id
     * @return effectedNum
     */
    int deleteShopAuthMap(long shopAuthId);

    /**
     * 通过shopAuthId查询员工授权信息
     *
     * @param shopAuthId shopAuthId
     * @return 员工授权信息
     */
    ShopAuthMap queryShopAuthMapById(Long shopAuthId);
}
