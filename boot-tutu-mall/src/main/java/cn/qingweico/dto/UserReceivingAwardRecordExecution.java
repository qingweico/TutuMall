package cn.qingweico.dto;

import cn.qingweico.entity.UserReceivingAwardRecord;
import cn.qingweico.enums.UserReceivingAwardRecordStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/15
 */
@Data
public class UserReceivingAwardRecordExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * //
     */
    private UserReceivingAwardRecord userReceivingAwardRecord;

    /**
     * //
     */
    private List<UserReceivingAwardRecord> userReceivingAwardRecordList;

    public UserReceivingAwardRecordExecution() {
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum UserAwardMapStateEnum
     */
    public UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum                UserAwardMapStateEnum
     * @param userReceivingAwardRecord userReceivingAwardRecord
     */
    public UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum stateEnum, UserReceivingAwardRecord userReceivingAwardRecord) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userReceivingAwardRecord = userReceivingAwardRecord;
    }

    /**
     * 批量操作成功时返回的构造器
     *
     * @param stateEnum                    UserAwardMapStateEnum
     * @param userReceivingAwardRecordList List<UserReceivingAwardRecord>
     */
    public UserReceivingAwardRecordExecution(UserReceivingAwardRecordStateEnum stateEnum, List<UserReceivingAwardRecord> userReceivingAwardRecordList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userReceivingAwardRecordList = userReceivingAwardRecordList;
    }
}
