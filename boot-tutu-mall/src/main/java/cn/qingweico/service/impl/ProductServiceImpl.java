package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductDao;
import cn.qingweico.dao.ProductImageDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductImage;
import cn.qingweico.enums.ProductStateEnum;
import cn.qingweico.exception.ProductOperationException;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/27
 */
@Service
public class ProductServiceImpl implements ProductService {
    ProductDao productDao;
    ProductImageDao productImageDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setProductImageDao(ProductImageDao productImageDao) {
        this.productImageDao = productImageDao;
    }

    /**
     * 添加商品
     * @param product          添加的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品详情图片列表
     * @return ProductExecution
     * @throws ProductOperationException ProductOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //商品的默认状态:上架
            product.setEnableStatus(1);
            //若前端上传的图片不为空则添加
            if (imageHolder != null) {
                addProductImage(product, imageHolder);
            }
            try {
                //创建商品信息
                int effectNumber = productDao.insertProduct(product);
                if (effectNumber <= 0) {
                    throw new ProductOperationException("创建商品失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败: " + e.getMessage());
            }
            //若前端传来的商品详情图不为空则添加
            if (productImageList != null && productImageList.size() > 0) {
                addProductImageList(product, productImageList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 修改商品信息
     * @param product          修改的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品图片列表
     * @return ProductExecution
     * @throws ProductOperationException ProductOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            if("".equals(product.getProductName()))  {
                return new ProductExecution(ProductStateEnum.EMPTY_PRODUCT_NAME);
            }
            product.setLastEditTime(new Date());
            //若前端上传的商品缩略图片不为空则删除原有的图片并更新图片
            try {
                if (imageHolder != null) {
                    Product tempProduct = productDao.queryProductById(product.getProductId());
                    if (tempProduct.getImageAddress() != null && !"".equals(tempProduct.getImageAddress())) {
                        ImageUtil.deleteFileOrDirectory(tempProduct.getImageAddress());
                    }
                    addProductImage(product, imageHolder);
                }
                //若前端传来的商品详情图不为空则删除原有的图片并更新图片
                if (productImageList != null && productImageList.size() > 0) {
                    try {
                        deleteProductImageList(product.getProductId());
                        addProductImageList(product, productImageList);
                    } catch (ProductOperationException e) {
                        throw new ProductOperationException("上传商品详情图失败！");
                    }
                }
            } catch (ProductOperationException e) {
                throw new ProductOperationException("上传商品图片失败！");
            }
            try {
                //创建商品信息
                int effectNumber = productDao.updateProduct(product);
                if (effectNumber <= 0) {
                    throw new ProductOperationException("更新商品失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("更新商品失败: " + e.getMessage());
            }

            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 根据商品id获取商品信息
     * @param productId 商品id
     * @return v
     */
    @Override
    public Product getProductById(Long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setProductList(productList);
        productExecution.setCount(count);
        return productExecution;
    }

    /**
     * 添加商品缩略图
     *
     * @param product     product
     * @param imageHolder imageHolder
     */
    public void addProductImage(Product product, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String productImageAddress = ImageUtil.generateThumbnails(imageHolder, dest);
        product.setImageAddress(productImageAddress);
    }

    /**
     * 批量添加商品
     */
    public void addProductImageList(Product product, List<ImageHolder> imageHolderList) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImage> productImageList = new ArrayList<>();
        for (ImageHolder holder : imageHolderList) {
            String imageAddress = ImageUtil.generateThumbnails(holder, dest);
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getProductId());
            productImage.setImageAddress(imageAddress);
            productImage.setCreateTime(new Date());
            productImageList.add(productImage);
        }
        if (productImageList.size() > 0) {
            try {
                int effectNumber = productImageDao.batchInsertProductImage(productImageList);
                if (effectNumber <= 0) {
                    throw new ProductOperationException("创建商品详情图失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图失败: " + e.getMessage());
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
            ImageUtil.deleteFileOrDirectory(productImage.getImageAddress());
        }
        productImageDao.deleteProductImageByProductId(productId);
    }
}
