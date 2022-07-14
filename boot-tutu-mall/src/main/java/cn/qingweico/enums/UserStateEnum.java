package cn.qingweico.enums;

/**
 * 用户操作状态枚举
 *
 * @author zqw
 * @date 2020/10/15
 */
public enum UserStateEnum {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "内部错误"),
    /**
     * 用户信息为空
     */
    EMPTY(-1002, "用户信息为空"),


    NO_EXIST(-1003, "不存在该用户");

    private final int state;

    private final String stateInfo;

    UserStateEnum(int state, String stateInfo) {
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
    public static UserStateEnum getStateEnum(int index) {
        for (UserStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}