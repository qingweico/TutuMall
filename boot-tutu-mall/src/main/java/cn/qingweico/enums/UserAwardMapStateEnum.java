package cn.qingweico.enums;

/**
 * 有关用户领取奖品操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/15
 */
public enum UserAwardMapStateEnum {
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * UserAwardId为空
     */
    NULL_USER_AWARD_ID(-1002, "UserAwardId为空"),
    /**
     * 空的信息
     */
    NULL_USER_AWARD_INFO(-1003, "空的信息");

    private final int state;

    private final String stateInfo;

    UserAwardMapStateEnum(int state, String stateInfo) {
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
    public static UserAwardMapStateEnum getStateEnum(int index) {
        for (UserAwardMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
