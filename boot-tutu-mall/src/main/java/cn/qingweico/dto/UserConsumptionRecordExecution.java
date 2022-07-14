package cn.qingweico.dto;


import cn.qingweico.entity.UserConsumptionRecord;
import cn.qingweico.enums.UserConsumptionRecordStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/8
 */
@Data
public class UserConsumptionRecordExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private UserConsumptionRecord userConsumptionRecord;
    private List<UserConsumptionRecord > userConsumptionRecordList;

    public UserConsumptionRecordExecution() {
    }

    /**
     * 添加一条用户消费记录
     *
     * @param stateEnum 成功或者失败时的状态
     */
    public UserConsumptionRecordExecution(UserConsumptionRecordStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 添加一条用户消费记录
     *
     * @param stateEnum             成功或者失败时的状态
     * @param userConsumptionRecord 用户消费记录
     */
    public UserConsumptionRecordExecution(UserConsumptionRecordStateEnum stateEnum, UserConsumptionRecord userConsumptionRecord) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userConsumptionRecord = userConsumptionRecord;
    }
}
