package cn.qingweico.dto;

import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.enums.UserShopMapStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/14
 */
@Data
public class UserPointRecordExecution {
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
    private UserPointRecord userPointRecord;

    private List<UserPointRecord> userPointRecordList;

    public UserPointRecordExecution() {
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum 错误状态信息
     */
    public UserPointRecordExecution(UserShopMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum       状态信息
     * @param userPointRecord userPointRecord
     */
    public UserPointRecordExecution(UserShopMapStateEnum stateEnum, UserPointRecord userPointRecord) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userPointRecord = userPointRecord;
    }
}
