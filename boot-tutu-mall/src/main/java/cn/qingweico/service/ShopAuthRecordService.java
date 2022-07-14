package cn.qingweico.service;

import cn.qingweico.dto.ShopAuthRecordExecution;
import cn.qingweico.entity.ShopAuthRecord;

/**
 * @author 周庆伟
 * @date 2020/10/4
 */

public interface ShopAuthRecordService {
    /**
     * 根据店铺Id分页显示该店铺的授权记录
     *
     * @param shopId    店铺Id
     * @param page 查询起始索引
     * @param pageSize  每页的数量
     * @return ShopAuthMapExecution
     */
    ShopAuthRecordExecution listShopAuthRecordByShopId(Long shopId, Integer page, Integer pageSize);

    /**
     * 根据shopAuthId返回对应的授权信息
     *
     * @param shopAuthId shopAuthId
     * @return ShopAuthMap
     */
    ShopAuthRecord getShopAuthRecordById(Long shopAuthId);

    /**
     * 添加授权信息
     *
     * @param shopAuthRecord ShopAuthRecord
     * @return ShopAuthMapExecution
     */
    ShopAuthRecordExecution addShopAuthRecord(ShopAuthRecord shopAuthRecord);

    /**
     * 更新授权信息
     *
     * @param shopAuthRecord shopAuthRecord
     * @return ShopAuthMapExecution
     */
    ShopAuthRecordExecution modifyShopAuthRecord(ShopAuthRecord shopAuthRecord);

}
