package cn.qingweico.service;

import cn.qingweico.dto.UserReceivingAwardRecordExecution;
import cn.qingweico.entity.UserReceivingAwardRecord;

/**
 * @author zqw
 * @date 2020/10/15
 */

public interface UserReceivingAwardRecordService {
    /**
     * 根据传入的查询条件分页获取用户领取奖品记录列表
     *
     * @param userAwardCondition 查询条件
     * @param page               起始索引
     * @param pageSize           每页的数量
     * @return UserAwardMapExecution
     */
    UserReceivingAwardRecordExecution listUserReceivedAwardRecord(UserReceivingAwardRecord userAwardCondition, Integer page, Integer pageSize);

    /**
     * 根据传入的id获取用户领取奖品信息
     *
     * @param id 主键id
     * @return UserReceivingAwardRecord
     */
    UserReceivingAwardRecord getUserReceivingAwardRecordById(long id);

    /**
     * 用户领取奖品,添加一条记录
     *
     * @param userReceivingAwardRecord UserReceivingAwardRecord
     * @return UserAwardMapExecution
     */
    UserReceivingAwardRecordExecution addUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord);

    /**
     * 修改用户领取奖品记录,主要用来修改奖品领取状态
     *
     * @param userReceivingAwardRecord UserReceivingAwardRecord
     * @return UserAwardMapExecution
     */
    UserReceivingAwardRecordExecution modifyUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord);

}
