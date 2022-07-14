package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductCategoryDao;
import cn.qingweico.dao.ProductDao;
import cn.qingweico.dto.ProductCategoryExecution;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.enums.ProductCategoryStateEnum;
import cn.qingweico.service.ProductCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/27
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    ProductDao productDao;
    @Resource
    ProductCategoryDao productCategoryDao;


    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) {
        int effectNumber = productCategoryDao.batchInsertProductCategory(productCategoryList);
        if (effectNumber > 0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductCategoryExecution deleteProductCategoryById(Long productCategoryId, Long shopId) {
        int effectNumber = productDao.updateProductCategoryToNull(productCategoryId);
        if (effectNumber <= 0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
        }
        effectNumber = productCategoryDao.deleteProductCategoryById(productCategoryId, shopId);
        if (effectNumber <= 0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
        }
        return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
    }
}
