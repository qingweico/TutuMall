package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.ShopAuthRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/3
 */

@Repository
public interface ShopAuthRecordDao {
    /**
     * 分页列出店铺下面的授权信息
     *
     * @param shopId   店铺id
     * @param rowIndex 起始索引
     * @param pageSize 每页数量
     * @return 店铺授权列表
     */
    List<ShopAuthRecord> queryShopAuthMapListByShopId(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
                                                      @Param("pageSize") int pageSize);

    /**
     * 新增一条店铺授权记录
     *
     * @param shopAuthRecord shopAuthRecord
     * @return effectedNum
     */
    int insertShopAuthRecord(ShopAuthRecord shopAuthRecord);

    /**
     * 更新授权信息
     *
     * @param shopAuthRecord shopAuthRecord
     * @return effectedNum
     */
    int updateShopAuthRecord(ShopAuthRecord shopAuthRecord);

    /**
     * 删除已授权记录
     *
     * @param shopAuthId 已授权记录id
     * @return effectedNum
     */
    int deleteShopAuthRecord(long shopAuthId);

    /**
     * 通过id查询授权记录
     *
     * @param shopAuthId shopAuthId
     * @return 店铺授权记录
     */
    ShopAuthRecord queryShopAuthRecordById(Long shopAuthId);
}
