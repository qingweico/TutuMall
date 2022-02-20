package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductCategoryDao;
import cn.qingweico.dao.ProductDao;
import cn.qingweico.dto.ProductCategoryExecution;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.enums.ProductCategoryStateEnum;
import cn.qingweico.exception.ProductCategoryOperationException;
import cn.qingweico.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/27
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    ProductDao productDao;

    ProductCategoryDao productCategoryDao;

    @Autowired
    public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<ProductCategory> getProductCategoryList(Integer shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                int effectNumber = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectNumber <= 0) {
                    throw new ProductCategoryOperationException("店铺类别创建失败!");
                } else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("批量添加商品类别错误!" + e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductCategoryExecution deleteProductCategoryById(Long productCategoryId, Integer shopId) throws ProductCategoryOperationException {
        try {
            int effectNumber = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectNumber < 0) {
                throw new ProductCategoryOperationException("所属商品的商品类别更新失败!");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("所属商品的商品类别更新失败: " + e.getMessage());
        }
        try {
            int effectNumber = productCategoryDao.deleteProductCategoryById(productCategoryId, shopId);
            if (effectNumber <= 0) {
                throw new ProductCategoryOperationException("商品类被删除失败!");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("商品类被删除失败! " + e.getMessage());
        }
    }

}
