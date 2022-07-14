package cn.qingweico.service.impl;


import java.util.Date;
import java.util.List;

import cn.qingweico.dao.ShopAuthRecordDao;
import cn.qingweico.dto.ShopAuthRecordExecution;
import cn.qingweico.entity.ShopAuthRecord;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.ShopAuthRecordStateEnum;
import cn.qingweico.service.ShopAuthRecordService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/10/4
 */

@Service
public class ShopAuthRecordServiceImpl implements ShopAuthRecordService {

    @Resource
    private ShopAuthRecordDao shopAuthRecordDao;

    /**
     * //
     *
     * @param shopId   店铺Id
     * @param page     查询起始索引
     * @param pageSize 每页的数量
     * @return ShopAuthRecordExecution
     */
    @Override
    public ShopAuthRecordExecution listShopAuthRecordByShopId(Long shopId, Integer page, Integer pageSize) {
        // 页转行
        int beginIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 查询返回该店铺的授权信息列表
        List<ShopAuthRecord> shopAuthMapList = shopAuthRecordDao.queryShopAuthMapListByShopId(shopId, beginIndex,
                pageSize);
        ShopAuthRecordExecution shopAuthMapExecution = new ShopAuthRecordExecution();
        shopAuthMapExecution.setShopAuthMapList(shopAuthMapList);
        return shopAuthMapExecution;

    }

    /**
     * 。。
     *
     * @param shopAuthId shopAuthId
     * @return ShopAuthRecord
     */
    @Override
    public ShopAuthRecord getShopAuthRecordById(Long shopAuthId) {
        return shopAuthRecordDao.queryShopAuthRecordById(shopAuthId);
    }

    /**
     * //
     *
     * @param shopAuthRecord {@link ShopAuthRecord}
     * @return ShopAuthRecordExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthRecordExecution addShopAuthRecord(ShopAuthRecord shopAuthRecord) {
        shopAuthRecord.setCreateTime(new Date());
        shopAuthRecord.setLastEditTime(new Date());
        shopAuthRecord.setEnableStatus(GlobalStatusEnum.available.getVal());
        int effectedNum = shopAuthRecordDao.insertShopAuthRecord(shopAuthRecord);
        if (effectedNum <= 0) {
            return new ShopAuthRecordExecution(ShopAuthRecordStateEnum.INNER_ERROR);
        }
        return new ShopAuthRecordExecution(ShopAuthRecordStateEnum.SUCCESS);
    }

    /**
     * //
     *
     * @param shopAuthRecord ShopAuthRecord
     * @return ShopAuthRecordExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthRecordExecution modifyShopAuthRecord(ShopAuthRecord shopAuthRecord) {
        shopAuthRecord.setLastEditTime(new Date());
        int effectedNum = shopAuthRecordDao.updateShopAuthRecord(shopAuthRecord);
        if (effectedNum <= 0) {
            return new ShopAuthRecordExecution(ShopAuthRecordStateEnum.INNER_ERROR);
        } else {
            return new ShopAuthRecordExecution(ShopAuthRecordStateEnum.SUCCESS);
        }
    }

}
