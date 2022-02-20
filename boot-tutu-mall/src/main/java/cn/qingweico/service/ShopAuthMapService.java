package cn.qingweico.service;

import cn.qingweico.dto.ShopAuthMapExecution;
import cn.qingweico.entity.ShopAuthMap;
import cn.qingweico.exception.ShopAuthMapOperationException;

/**
 * @author 周庆伟
 * @date 2020/10/4
 */

public interface ShopAuthMapService {
    /**
     * 根据店铺Id分页显示该店铺的授权信息
     *
     * @param shopId    店铺Id
     * @param pageIndex 查询起始索引
     * @param pageSize  每页的数量
     * @return ShopAuthMapExecution
     */
    ShopAuthMapExecution listShopAuthMapByShopId(Integer shopId, Integer pageIndex, Integer pageSize);

    /**
     * 根据shopAuthId返回对应的授权信息
     *
     * @param shopAuthId shopAuthId
     * @return ShopAuthMap
     */
    ShopAuthMap getShopAuthMapById(Long shopAuthId);

    /**
     * 添加授权信息
     *
     * @param shopAuthMap ShopAuthMap
     * @return ShopAuthMapExecution
     * @throws ShopAuthMapOperationException ShopAuthMapOperationException
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    /**
     * 更新授权信息，包括职位，状态等
     *
     * @param shopAuthMap ShopAuthMap
     * @return ShopAuthMapExecution
     * @throws ShopAuthMapOperationException ShopAuthMapOperationException
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

}
