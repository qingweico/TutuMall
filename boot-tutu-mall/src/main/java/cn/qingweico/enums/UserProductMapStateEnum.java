package cn.qingweico.enums;

/**
 * 有关用户购买商品操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/8
 */

public enum UserProductMapStateEnum {
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * UserProductId为空
     */
    NULL_USER_PRODUCT_ID(-1002, "UserProductId为空"),
    /**
     * 空的信息
     */
    NULL_USER_PRODUCT_INFO(-1003, "空的信息");

    private final int state;

    private final String stateInfo;

    UserProductMapStateEnum(int state, String stateInfo) {
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
    public static UserProductMapStateEnum getStateEnum(int index) {
        for (UserProductMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
