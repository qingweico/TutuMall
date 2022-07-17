package cn.qingweico.service;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/26
 */
public interface ShopCategoryService {

    /**
     * 根据查询条件获取店铺类别列表
     *
     * @param condition 查询条件
     * @return 店铺类别列表
     */
    List<ShopCategory> getShopCategoryList(ShopCategory condition);

    /**
     * 添加店铺类别
     *
     * @param shopCategory 店铺类别
     * @param thumbnail    店铺缩略图
     * @return ShopCategoryExecution
     */
    ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);

    /**
     * 修改店铺类别
     *
     * @param shopCategory 店铺类别
     * @param thumbnail    店铺缩略图
     * @return ShopCategoryExecution
     */
    ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);

    /**
     * 根据id返回店铺类别信息
     *
     * @param shopCategoryId 店铺类别id
     * @return ShopCategory
     */
    ShopCategory getShopCategoryById(Long shopCategoryId);
}
