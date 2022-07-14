package cn.qingweico.dao;

import cn.qingweico.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/10
 */
@Repository
public interface ProductCategoryDao {
    /**
     * 根据shop id来查询店铺商品分类
     *
     * @param shopId 店铺id
     * @return 店铺列表
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     * 批量新增商品类别
     *
     * @param productCategoryList 商品类别列表
     * @return 新增的商品类别种数
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 根据商品类别id删除商品类别
     *
     * @param productCategoryId productCategoryId
     * @param shopId            shopId
     * @return effectNum
     */
    int deleteProductCategoryById(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);
}
