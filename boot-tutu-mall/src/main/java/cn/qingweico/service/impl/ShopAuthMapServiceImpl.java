package cn.qingweico.service.impl;


import java.util.Date;
import java.util.List;

import cn.qingweico.dao.ShopAuthMapDao;
import cn.qingweico.dto.ShopAuthMapExecution;
import cn.qingweico.entity.ShopAuthMap;
import cn.qingweico.enums.ShopAuthMapStateEnum;
import cn.qingweico.exception.ShopAuthMapOperationException;
import cn.qingweico.service.ShopAuthMapService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 周庆伟
 * @date 2020/10/4
 */

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    private ShopAuthMapDao shopAuthMapDao;

    @Autowired
    public void setShopAuthMapDao(ShopAuthMapDao shopAuthMapDao) {
        this.shopAuthMapDao = shopAuthMapDao;
    }

    /**
     * 根据店铺id查询该店铺下的授权列表
     * @param shopId    店铺Id
     * @param pageIndex 查询起始索引
     * @param pageSize  每页的数量
     * @return ShopAuthMapExecution
     */
    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Integer shopId, Integer pageIndex, Integer pageSize) {
        // 空值判断
        if (shopId != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
            // 查询返回该店铺的授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex,
                    pageSize);
            // 返回总数
            int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
            ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();
            shopAuthMapExecution.setShopAuthMapList(shopAuthMapList);
            shopAuthMapExecution.setCount(count);
            return shopAuthMapExecution;
        } else {
            return null;
        }

    }

    /**
     * 根据授权id查询授权信息与店铺之间的关系映射
     * @param shopAuthId shopAuthId
     * @return 授权信息与店铺之间的关系映射
     */
    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

    /**
     * 添加一条店铺授权记录
     * @param shopAuthMap ShopAuthMap
     * @return ShopAuthMapExecution
     * @throws ShopAuthMapOperationException ShopAuthMapOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        // 空值判断, 主要是对店铺Id和员工Id做校验
        if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
                && shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            try {
                // 添加授权信息
                int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    throw new ShopAuthMapOperationException("添加授权失败");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("添加授权失败:" + e);
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_INFO);
        }
    }

    /**
     * 修改店铺下已授权的信息
     * @param shopAuthMap ShopAuthMap
     * @return ShopAuthMapExecution
     * @throws ShopAuthMapOperationException ShopAuthMapOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        // 空值判断, 主要是对授权Id做校验
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_ID);
        } else {
            try {
                shopAuthMap.setLastEditTime(new Date());
                int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
                } else {
                    // 创建成功
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
                }
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("modifyShopAuthMap error: " + e.getMessage());
            }
        }
    }

}
