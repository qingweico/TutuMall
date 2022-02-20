package cn.qingweico.service.impl;

import cn.qingweico.dao.ProductSellDailyDao;
import cn.qingweico.entity.ProductSellDaily;
import cn.qingweico.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/8
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    private final static Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    ProductSellDailyDao productSellDailyDao;

    @Autowired
    public void setProductSellDailyDao(ProductSellDailyDao productSellDailyDao) {
        this.productSellDailyDao = productSellDailyDao;
    }

    @Override
    public void dailyCalculate() {
        logger.info("quartz has running...");
        //统计每个店铺有销售量的商品的日销售量
        productSellDailyDao.insertProductSellDaily();
        //统计每个店铺中剩下的没有销售量的商品的日销售量, 全部置为零
        productSellDailyDao.insertDefaultProductSellDaily();

    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }
}
