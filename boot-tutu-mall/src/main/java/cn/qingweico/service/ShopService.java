package cn.qingweico.service;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.exception.ShopOperationException;


/**
 * @author 周庆伟\
 * @date 2020/09/15
 */
public interface ShopService {
    /**
     * 注册店铺信息 包括图片处理 的业务
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     * @throws ShopOperationException ShopOperationException
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

    /**
     * 根据店铺Id获取店铺信息
     *
     * @param shopId 店铺id
     * @return 店铺对象
     */
    Shop getShopById(Integer shopId);

    /**
     * 更新店铺信息 包括对图片的处理
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder);

    /**
     * 根据shopCondition分页返回相应的店铺列表
     *
     * @param shopCondition 查询条件
     * @param pageIndex     开始查询的位置
     * @param pageSize      每页的数量
     * @return ShopExecution
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    /**
     * 更新店铺状态
     *
     * @param shop Shop
     * @return ShopExecution
     */
    ShopExecution updateShopStatus(Shop shop);
}
