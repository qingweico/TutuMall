package cn.qingweico.service;

import cn.qingweico.dto.UserConsumptionRecordExecution;
import cn.qingweico.entity.UserConsumptionRecord;

/**
 * @author zqw
 * @date 2020/10/8
 */

public interface UserConsumptionRecordService {
    /**
     * 通过传入的查询条件分页列出用户消费信息记录
     *
     * @param condition 查询条件
     * @param page                 开始查询的索引
     * @param pageSize             每页的数量
     * @return UserProductMapExecution
     */
    UserConsumptionRecordExecution userConsumptionRecordList(UserConsumptionRecord condition, Integer page, Integer pageSize);

    /**
     * 添加一条用户消费记录
     *
     * @param userConsumptionRecord 用户消费记录
     * @return UserProductMapExecution
     */
    UserConsumptionRecordExecution addUserConsumptionRecord(UserConsumptionRecord userConsumptionRecord);
}
