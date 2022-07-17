package cn.qingweico.dao;

import cn.qingweico.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/17
 */
@Repository
public interface ShopDao {
    /**
     * 注册店铺
     *
     * @param shop shop
     * @return effectNum
     */
    int registerShop(Shop shop);

    /**
     * 更新店铺
     *
     * @param shop shop
     * @return effectNum
     */
    int updateShop(Shop shop);

    /**
     * 通过店铺id查询店铺
     *
     * @param shopId 店铺id
     * @return shop
     */
    Shop queryShopById(Long shopId);

    /**
     * 分页查询店铺列表 根据店铺分类 区域 店铺状态 店铺名称(模糊查询)以及user
     *
     * @param shopCondition 查询条件
     * @param rowIndex      开始取数据的行数(从0开始)
     * @param pageSize      返回的条数
     * @return 店铺列表
     */
    List<Shop> queryShopList(@Param("condition") Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询所有的店铺信息
     *
     * @param rowIndex 分页的起始索引
     * @param pageSize 每页的店铺数量
     * @return List<Shop>
     */
    List<Shop> queryAllShop(Integer rowIndex, Integer pageSize);

    /**
     * 更新店铺状态
     *
     * @param shop Shop
     * @return effectNum
     */
    int updateShopStatus(Shop shop);
}
