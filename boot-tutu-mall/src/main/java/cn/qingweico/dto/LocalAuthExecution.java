package cn.qingweico.dto;

import cn.qingweico.entity.LocalAuth;
import cn.qingweico.enums.LocalAuthStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/11/11
 */
@Data
public class LocalAuthExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;

    private int count;

    private LocalAuth localAuth;

    private List<LocalAuth> localAuthList;

    /**
     * @param stateEnum LocalAuthStateEnum
     */
    public LocalAuthExecution(LocalAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 有关本地账号操作成功时返回的构造器
     *
     * @param stateEnum LocalAuthStateEnum
     * @param localAuth LocalAuth
     */
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, LocalAuth localAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuth = localAuth;
    }
}
