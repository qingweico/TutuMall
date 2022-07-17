package cn.qingweico.service.impl;

import cn.qingweico.dao.UserConsumptionRecordDao;
import cn.qingweico.dao.UserPointRecordDao;
import cn.qingweico.dto.UserConsumptionRecordExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserConsumptionRecord;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.enums.UserConsumptionRecordStateEnum;
import cn.qingweico.service.UserConsumptionRecordService;


import java.util.Date;
import java.util.List;

import cn.qingweico.utils.PageCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/10/9
 */
@Slf4j
@Service
public class UserConsumptionRecordServiceImpl implements UserConsumptionRecordService {

    @Resource
    private UserConsumptionRecordDao userConsumptionRecordDao;
    @Resource
    private UserPointRecordDao userPointRecordDao;

    /**
     * 列出用户所购买的所有商品
     *
     * @param userProductCondition 查询条件
     * @param page                 开始查询的索引
     * @param pageSize             每页的数量
     * @return UserProductMapExecution
     */
    @Override
    public UserConsumptionRecordExecution userConsumptionRecordList(UserConsumptionRecord userProductCondition, Integer page, Integer pageSize) {
        // 页转行
        int beginIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 依据查询条件分页取出列表
        List<UserConsumptionRecord> userConsumptionRecordList = userConsumptionRecordDao.queryUserConsumptionRecordList(userProductCondition, beginIndex, pageSize);
        UserConsumptionRecordExecution userProductMapExecution = new UserConsumptionRecordExecution();
        userProductMapExecution.setUserConsumptionRecordList(userConsumptionRecordList);
        return userProductMapExecution;

    }

    /**
     * 添加消费记录
     *
     * @param userConsumptionRecord 用户消费记录
     * @return UserProductMapExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserConsumptionRecordExecution addUserConsumptionRecord(UserConsumptionRecord userConsumptionRecord) {
        userConsumptionRecord.setCreateTime(new Date());
        // 添加消费记录
        int effectedNum = userConsumptionRecordDao.insertUserConsumptionRecord(userConsumptionRecord);
        if (effectedNum <= 0) {
            return new UserConsumptionRecordExecution(UserConsumptionRecordStateEnum.INNER_ERROR);
        }
        // 若本次消费能够积分
        if (userConsumptionRecord.getPoint() != null) {
            // 查询该顾客是否在店铺消费过
            UserPointRecord userPointRecord = userPointRecordDao.queryUserPointRecord(userConsumptionRecord.getUserId(),
                    userConsumptionRecord.getShopId());
            if (userPointRecord != null) {
                // 若之前消费过，即有过积分记录，则进行总积分的更新操作
                userPointRecord.setPoint(userPointRecord.getPoint() + userConsumptionRecord.getPoint());
                effectedNum = userPointRecordDao.updateUserPointRecord(userPointRecord);
            } else {
                // 在店铺没有过消费记录，添加一条店铺积分信息
                userPointRecord = compactUserPointRecordCondition(userConsumptionRecord.getUserId(),
                        userConsumptionRecord.getShopId(), userConsumptionRecord.getPoint());
                effectedNum = userPointRecordDao.insertUserPointRecord(userPointRecord);
            }
            if (effectedNum <= 0) {
                return new UserConsumptionRecordExecution(UserConsumptionRecordStateEnum.INNER_ERROR);
            }
        }
        return new UserConsumptionRecordExecution(UserConsumptionRecordStateEnum.SUCCESS);
    }


    /**
     * 封装顾客积分信息
     *
     * @param userId 顾客id
     * @param shopId 店铺id
     * @param point  积分
     * @return UserShopMap
     */
    private UserPointRecord compactUserPointRecordCondition(Long userId, Long shopId, Integer point) {
        UserPointRecord userPointRecord = null;
        // 空值判断
        if (userId != null && shopId != null) {
            userPointRecord = new UserPointRecord();
            User user = new User();
            user.setId(userId);
            Shop shop = new Shop();
            shop.setId(shopId);
            userPointRecord.setUser(user);
            userPointRecord.setShop(shop);
            userPointRecord.setCreateTime(new Date());
            userPointRecord.setPoint(point);
        }
        return userPointRecord;
    }

}
