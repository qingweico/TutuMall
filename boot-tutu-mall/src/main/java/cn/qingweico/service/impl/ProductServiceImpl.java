package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductDao;
import cn.qingweico.dao.ProductImageDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductImage;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.ProductStateEnum;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/27
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    ProductDao productDao;
    @Resource
    ProductImageDao productImageDao;

    /**
     * 添加商品
     *
     * @param product          添加的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品详情图片列表
     * @return ProductExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList) {
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        //商品的默认状态:上架
        product.setEnableStatus(GlobalStatusEnum.available.getVal());
        //若前端上传的图片不为空则添加
        if (imageHolder != null) {
            addProductImage(product, imageHolder);
        }
        if (productDao.insertProduct(product) <= 0) {
            return new ProductExecution(ProductStateEnum.INNER_ERROR);
        }
        // 若前端传来的商品详情图不为空则添加
        if (productImageList.size() > 0) {
            addProductImageList(product, productImageList);
        }
        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    /**
     * 修改商品信息
     *
     * @param product          修改的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品图片列表
     * @return ProductExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList) {
        product.setLastEditTime(new Date());
        // 若前端上传的商品缩略图片不为空则删除原有的图片并更新图片
        if (imageHolder != null) {
            Product tempProduct = productDao.queryProductById(product.getId());
            if (tempProduct.getImgUrl() != null && !StringUtils.EMPTY.equals(tempProduct.getImgUrl())) {
                ImageUtil.deleteFileOrDirectory(tempProduct.getImgUrl());
            }
            addProductImage(product, imageHolder);
        }
        // 若前端传来的商品详情图不为空则删除原有的图片并更新图片
        if (productImageList.size() > 0) {
            deleteProductImageList(product.getId());
            addProductImageList(product, productImageList);
        }
        if (productDao.updateProduct(product) <= 0) {
            return new ProductExecution(ProductStateEnum.INNER_ERROR);
        }
        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    /**
     * 根据商品id获取商品信息
     *
     * @param productId 商品id
     * @return Product
     */
    @Override
    public Product getProductById(Long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int page, int pageSize) {
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setProductList(productList);
        return productExecution;
    }

    /**
     * 添加商品缩略图
     *
     * @param product     product
     * @param imageHolder imageHolder
     */
    public void addProductImage(Product product, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(product.getShopId());
        String productImageAddress = ImageUtil.generateThumbnails(imageHolder, dest);
        product.setImgUrl(productImageAddress);
    }

    /**
     * 批量添加商品缩略图
     */
    public void addProductImageList(Product product, List<ImageHolder> imageHolderList) {
        String dest = PathUtil.getShopImagePath(product.getShopId());
        List<ProductImage> productImageList = new ArrayList<>();
        for (ImageHolder holder : imageHolderList) {
            String imageAddress = ImageUtil.generateThumbnails(holder, dest);
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getId());
            productImage.setImgUrl(imageAddress);
            productImage.setCreateTime(new Date());
            productImageList.add(productImage);
        }
        if (productImageList.size() > 0) {
            int effectNumber = productImageDao.batchInsertProductImage(productImageList);
            if (effectNumber <= 0) {
                log.error("addProductImageList error");
            }
        }
    }

    /**
     * 删除某个商品下所有的商品详情图
     *
     * @param productId 商品id
     */
    public void deleteProductImageList(Long productId) {
        List<ProductImage> productImageList = productImageDao.queryProductImageList(productId);
        for (ProductImage productImage : productImageList) {
            ImageUtil.deleteFileOrDirectory(productImage.getImgUrl());
        }
        int effectNumber = productImageDao.deleteProductImageByProductId(productId);
        if (effectNumber <= 0) {
            log.error("deleteProductImageList error");
        }
    }
}
