package cn.qingweico.dao;

import cn.qingweico.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */
@Repository
public interface ShopCategoryDao {
    /**
     * 查询店铺列表
     *
     * @param shopCategoryCondition 查询条件
     * @return 店铺列表
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

    /**
     * 通过Id返回唯一的店铺类别信息
     *
     * @param shopCategoryId 店铺类别id
     * @return ShopCategory
     */
    ShopCategory queryShopCategoryById(long shopCategoryId);

    /**
     * 根据传入的id列表查询店铺类别信息(供超级管理员选定删除类别的时候用,主要是处理图片)
     *
     * @param shopCategoryIdList 店铺类别id集合
     * @return 店铺类别集合
     */
    List<ShopCategory> queryShopCategoryByIds(List<Long> shopCategoryIdList);

    /**
     * 插入一条店铺类别信息
     *
     * @param shopCategory 店铺类别
     * @return effectNum
     */
    int insertShopCategory(ShopCategory shopCategory);

    /**
     * 更新店铺类别信息
     *
     * @param shopCategory 店铺类别
     * @return effectNum
     */
    int updateShopCategory(ShopCategory shopCategory);

    /**
     * 删除店铺类别
     *
     * @param shopCategoryId 店铺类别id
     * @return effectNum
     */
    int deleteShopCategory(long shopCategoryId);

    /**
     * 批量删除店铺类别
     *
     * @param shopCategoryIdList 店铺类别id集合
     * @return effectNum
     */
    int batchDeleteShopCategory(List<Long> shopCategoryIdList);
}
