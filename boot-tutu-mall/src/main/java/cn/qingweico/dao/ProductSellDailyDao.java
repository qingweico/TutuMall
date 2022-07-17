package cn.qingweico.dao;

import java.util.Date;
import java.util.List;

import cn.qingweico.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/3
 */

@Repository
public interface ProductSellDailyDao {
    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyCondition 查询条件
     * @param beginTime                 开始时间
     * @param endTime                   结束时间
     * @return 商品日销售的统计列表
     */
    List<ProductSellDaily> queryProductSellDailyList(
            @Param("condition") ProductSellDaily productSellDailyCondition,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 统计平台所有商品的日销售量
     *
     * @return effectNum
     */
    int insertProductSellDaily();

    /**
     * 统计平台当天没销量的商品 补全信息 将销量置为0
     *
     * @return effectNum
     */
    int insertDefaultProductSellDaily();
}
