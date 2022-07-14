package cn.qingweico.enums;

/**
 * 有关用户购买商品操作状态枚举
 *
 * @author zqw
 * @date 2020/10/8
 */

public enum UserConsumptionRecordStateEnum {
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部错误");
    private final int state;

    private final String stateInfo;

    UserConsumptionRecordStateEnum(int state, String stateInfo) {
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
    public static UserConsumptionRecordStateEnum getStateEnum(int index) {
        for (UserConsumptionRecordStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
