package cn.qingweico.dao;

import cn.qingweico.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/11
 */
@Repository
public interface ProductDao {
    /**
     * 查询商品列表并分页 查询条件包括 商品名称 商品id 商品状态 商品类别
     *
     * @param productCondition 查询商品的条件
     * @param rowIndex         起始查询的索引
     * @param pageSize         每页的商品数量
     * @return 商品列表
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 添加新的商品
     *
     * @param product 添加的商品对象
     * @return effectNum
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product 更新的商品对象
     * @return effectNum
     */
    int updateProduct(Product product);

    /**
     * 通过商品id查询商品信息
     *
     * @param productId 商品id
     * @return 商品对象
     */
    Product queryProductById(Long productId);

    /**
     * 删除商品类别时先将该类别下所有商品的商品类别置为空
     *
     * @param productCategoryId 商品类别id
     * @return effectNum
     */
    int updateProductCategoryToNull(Long productCategoryId);
}
