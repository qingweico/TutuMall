package cn.qingweico.enums;

/**
 * 有关用户领取奖品操作状态枚举
 *
 * @author zqw
 * @date 2020/10/15
 */
public enum UserReceivingAwardRecordStateEnum {
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部错误"),

    POINT_NOT_ENOUGH(-1002, "积分不足无法领取"),
    NON_POINT(-1003, "在本店铺没有积分, 无法对换奖品");
    private final int state;

    private final String stateInfo;

    UserReceivingAwardRecordStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static UserReceivingAwardRecordStateEnum getStateEnum(int index) {
        for (UserReceivingAwardRecordStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
