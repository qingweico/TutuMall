package cn.qingweico.service;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/26
 */
public interface ShopCategoryService {

    String SHOP_CATEGORY_LIST_KEY = "shopCategoryList";

    /**
     * 根据查询条件获取店铺类别列表的业务
     *
     * @param shopCategoryCondition 查询条件
     * @return 店铺类别列表
     */
    List<ShopCategory> getShopCategoryList(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

    /**
     * 添加店铺类别，并存储店铺类别图片
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
     * 根据Id返回店铺类别信息
     *
     * @param shopCategoryId 店铺类别id
     * @return ShopCategory
     */
    ShopCategory getShopCategoryById(Integer shopCategoryId);
}
