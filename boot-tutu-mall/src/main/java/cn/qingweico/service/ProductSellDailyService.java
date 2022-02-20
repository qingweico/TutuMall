package cn.qingweico.service;

import cn.qingweico.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/8
 */
public interface ProductSellDailyService {
    /**
     * 统计店铺的日销售量
     */
    void dailyCalculate();

    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyCondition 查询条件
     * @param beginTime                 开始统计时间
     * @param endTime                   统计结束时间
     * @return 商品日销售的统计列表
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);
}
