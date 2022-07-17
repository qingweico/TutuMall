package cn.qingweico.service;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Shop;


/**
 * @author zqw
 * @date 2020/09/15
 */
public interface ShopService {
    /**
     * 注册店铺
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder);

    /**
     * 根据店铺id获取店铺信息
     *
     * @param shopId 店铺id
     * @return 店铺对象
     */
    Shop getShopById(Long shopId);

    /**
     * 更新店铺信息
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder);

    /**
     * 根据shopCondition分页返回相应的店铺列表
     *
     * @param condition 查询条件
     * @param page          开始查询的位置
     * @param pageSize      每页的数量
     * @return ShopExecution
     */
    ShopExecution getShopList(Shop condition, int page, int pageSize);

    /**
     * 更新店铺状态
     *
     * @param shop Shop
     * @return ShopExecution
     */
    ShopExecution updateShopStatus(Shop shop);
}
