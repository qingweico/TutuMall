package cn.qingweico.service;

import cn.qingweico.dto.ProductCategoryExecution;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.exception.ProductCategoryOperationException;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/17
 */
public interface ProductCategoryService {
    /**
     * 根据店铺id查询店铺下所有商品类别的业务
     *
     * @param shopId 店铺id
     * @return 商品类别列表
     */
    List<ProductCategory> getProductCategoryList(Integer shopId);

    /**
     * 批量添加商品类别信息的业务
     *
     * @param productCategoryList 商品类别列表
     * @return ProductCategoryExecution
     * @throws ProductCategoryOperationException ProductCategoryOperationException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    /**
     * 根据商品类别id以及所属店铺id来删除商品类别的业务
     *
     * @param productCategoryId 商品类别id
     * @param shopId            店铺id
     * @return ProductCategoryExecution
     * @throws ProductCategoryOperationException ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategoryById(Long productCategoryId, Integer shopId) throws ProductCategoryOperationException;
}
