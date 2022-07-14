package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductSellDailyDao;
import cn.qingweico.entity.ProductSellDaily;
import cn.qingweico.service.ProductSellDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/10/8
 */
@Slf4j
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    @Resource
    ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        log.info("quartz has running...");
        // 统计每个店铺有销售量的商品的日销售量
        productSellDailyDao.insertProductSellDaily();
        // 统计每个店铺中剩下的没有销售量的商品的日销售量(全部置为零)
        productSellDailyDao.insertDefaultProductSellDaily();

    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }
}
