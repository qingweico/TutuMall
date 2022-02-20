package cn.qingweico.enums;

/**
 * 有关奖品操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/15
 */

public enum AwardStateEnum {
    /**
     * 非法奖品
     */
    OFFLINE(-1, "非法奖品"),
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * 奖品信息为空!
     */
    EMPTY(-1002, "奖品信息为空"),
    /**
     * 奖品信息为空!
     */
    EMPTY_AWARD_NAME(-1003, "奖品名称为空");

    private final int state;

    private final String stateInfo;

    AwardStateEnum(int state, String stateInfo) {
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
    public static AwardStateEnum getStateEnum(int index) {
        for (AwardStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
