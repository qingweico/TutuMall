package cn.qingweico.service.impl;

import cn.qingweico.dao.UserPointRecordDao;
import cn.qingweico.dto.UserPointRecordExecution;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.service.UserPointRecordService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2020/10/14
 */


@Service
public class UserPointRecordMapServiceImpl implements UserPointRecordService {

    @Resource
    private UserPointRecordDao userPointRecordDao;

    /**
     * @param userPointRecord 用户积分记录
     * @param page            查询起始索引
     * @param pageSize        每页的数量
     * @return UserPointRecordExecution
     */
    @Override
    public UserPointRecordExecution userPointRecordList(UserPointRecord userPointRecord, int page, int pageSize) {
        int beginIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 根据传入的查询条件分页返回用户积分列表信息
        List<UserPointRecord> userPointRecordList = userPointRecordDao.queryUserShopPointList(userPointRecord, beginIndex, pageSize);
        UserPointRecordExecution userPointRecordExecution = new UserPointRecordExecution();
        userPointRecordExecution.setUserPointRecordList(userPointRecordList);
        return userPointRecordExecution;

    }

    @Override
    public UserPointRecord getUserPointRecord(long userId, long shopId) {
        return userPointRecordDao.queryUserPointRecord(userId, shopId);
    }
}
