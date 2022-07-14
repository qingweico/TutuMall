package cn.qingweico.service.impl;


import cn.qingweico.dao.UserReceivingAwardRecordDao;
import cn.qingweico.dao.UserPointRecordDao;
import cn.qingweico.dto.UserReceivingAwardRecordExecution;
import cn.qingweico.entity.UserReceivingAwardRecord;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.UserReceivingAwardRecordStateEnum;
import cn.qingweico.service.UserReceivingAwardRecordService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/10/15
 */

@Service
public class UserReceivingAwardRecordServiceImpl implements UserReceivingAwardRecordService {

    @Resource
    private UserReceivingAwardRecordDao userReceivingAwardRecordDao;
    @Resource
    private UserPointRecordDao userPointRecordDao;


    /**
     * 列出用户已兑换的所有奖品
     *
     * @param userReceivingAwardRecord 查询条件
     * @param page                     起始索引
     * @param pageSize                 每页的数量
     * @return UserAwardMapExecution
     */
    @Override
    public UserReceivingAwardRecordExecution listUserReceivedAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord,
                                                                         Integer page, Integer pageSize) {
        int beginIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
        List<UserReceivingAwardRecord> userReceivingAwardRecordList = userReceivingAwardRecordDao.queryUserReceivingAwardRecordList(userReceivingAwardRecord, beginIndex, pageSize);
        UserReceivingAwardRecordExecution userAwardMapExecution = new UserReceivingAwardRecordExecution();
        userAwardMapExecution.setUserReceivingAwardRecordList(userReceivingAwardRecordList);
        return userAwardMapExecution;
    }


    @Override
    public UserReceivingAwardRecord getUserReceivingAwardRecordById(long userAwardMapId) {
        return userReceivingAwardRecordDao.queryUserReceivingAwardRecordById(userAwardMapId);
    }

    /**
     * 添加一条用户兑换奖品的记录
     *
     * @param userReceivingAwardRecord UserReceivingAwardRecord
     * @return UserReceivingAwardRecordExecution {@link UserReceivingAwardRecordExecution}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserReceivingAwardRecordExecution addUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord) {
        // 判断userId和shopId不为空
        userReceivingAwardRecord.setCreateTime(new Date());
        userReceivingAwardRecord.setUsedStatus(GlobalStatusEnum.disable.getVal());
        int effectedNum;
        if (userReceivingAwardRecord.getPoint() != null) {
            // 根据用户id和店铺id获取该用户在店铺的积分
            UserPointRecord userPoint = userPointRecordDao.queryUserPointRecord(userReceivingAwardRecord.getUserId(),
                    userReceivingAwardRecord.getShopId());
            // 判断该用户在店铺里是否有积分
            if (userPoint != null) {
                // 若有积分, 必须确保店铺积分大于本次要兑换奖品需要的积分
                if (userPoint.getPoint() >= userReceivingAwardRecord.getPoint()) {
                    // 积分抵扣
                    userPoint.setPoint(userPoint.getPoint() - userReceivingAwardRecord.getPoint());
                    // 更新积分信息
                    effectedNum = userPointRecordDao.updateUserPointRecord(userPoint);
                    if (effectedNum <= 0) {
                        return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.INNER_ERROR);
                    }
                } else {
                    return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.POINT_NOT_ENOUGH);
                }
            } else {
                // 在店铺没有积分, 则抛出异常
                return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.NON_POINT);
            }
        }
        effectedNum = userReceivingAwardRecordDao.insertUserReceivingAwardRecord(userReceivingAwardRecord);
        if (effectedNum <= 0) {
            return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.INNER_ERROR);
        }
        return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.SUCCESS);
    }

    /**
     * 修改奖品的领取状态
     *
     * @param userReceivingAwardRecord UserReceivingAwardRecord
     * @return UserReceivingAwardRecordExecution {@link UserReceivingAwardRecordExecution}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserReceivingAwardRecordExecution modifyUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord) {
        // 更新可用状态
        int effectedNum = userReceivingAwardRecordDao.updateUserReceivingAwardRecord(userReceivingAwardRecord);
        if (effectedNum <= 0) {
            return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.INNER_ERROR);
        } else {
            return new UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum.SUCCESS);
        }
    }

}
