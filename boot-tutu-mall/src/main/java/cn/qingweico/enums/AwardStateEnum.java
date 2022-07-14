package cn.qingweico.enums;

/**
 * 有关奖品操作状态枚举
 *
 * @author zqw
 * @date 2020/10/15
 */

public enum AwardStateEnum {
    /**
     * 非法奖品
     */
    UPDATE_SUCCESS(-1, "修改商品成功"),
    /**
     * 添加商品成功
     */
    ADD_SUCCESS(200, "添加商品成功"),
    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部错误");

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
