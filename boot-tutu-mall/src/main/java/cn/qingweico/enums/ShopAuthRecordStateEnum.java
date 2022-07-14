package cn.qingweico.enums;

/**
 * 有关店铺授权操作状态枚举
 *
 * @author zqw
 * @date 2020/10/4
 */

public enum ShopAuthRecordStateEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 失败
     */
    INNER_ERROR(-1001, "内部错误"),
    /**
     * token为空
     */
    NULL_TOKEN(-1004, "token为空");

    private final int state;

    private final String stateInfo;

    ShopAuthRecordStateEnum(int state, String stateInfo) {
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
    public static ShopAuthRecordStateEnum getStateEnum(int index) {
        for (ShopAuthRecordStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
