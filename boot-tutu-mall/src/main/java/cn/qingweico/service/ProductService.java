package cn.qingweico.service;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;

import java.util.List;

/**
 * @author zqw
 * @date 2020/09/26
 */
public interface ProductService {
    /**
     * 添加商品信息
     *
     * @param product          添加的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品详情图片列表
     * @return ProductExecution
     */
    ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList);

    /**
     * 修改商品信息
     *
     * @param product          修改的商品
     * @param imageHolder      商品缩略图
     * @param productImageList 商品图片列表
     * @return ProductExecution
     */
    ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImageList);


    /**
     * 根据商品id查询唯一的商品信息
     *
     * @param productId 商品id
     * @return 商品对象
     */
    Product getProductById(Long productId);

    /**
     * 查询商品列表并分页
     *
     * @param productCondition 查询条件: 店铺id 商品类别 商品状态 商品名称(模糊)
     * @param page             起始的页码
     * @param pageSize         每页的数量
     * @return ProductExecution
     */
    ProductExecution getProductList(Product productCondition, int page, int pageSize);
}
