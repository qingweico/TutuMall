package cn.qingweico.dao;

import cn.qingweico.entity.ProductImage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/12
 */
@Repository
public interface ProductImageDao {
    /**
     * 查询商品详情图片列表
     *
     * @param productId 商品id
     * @return 返回所查询商品下所有的图片集合
     */
    List<ProductImage> queryProductImageList(Long productId);

    /**
     * 批量插入商品图片
     *
     * @param productImageList 商品图片集合
     * @return effectNum
     */
    int batchInsertProductImage(List<ProductImage> productImageList);

    /**
     * 根据商品id删除所有的商品详情图片
     *
     * @param productId 商品id
     * @return effectNum
     */
    int deleteProductImageByProductId(Long productId);
}
