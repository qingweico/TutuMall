package cn.qingweico.enums;

/**
 * 有关用户店铺操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/14
 */
public enum UserShopMapStateEnum {
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * UserShopId为空
     */
    NULL_USER_SHOP_ID(-1002, "UserShopId为空"),
    /**
     * 空的信息
     */
    NULL_USER_SHOP_INFO(-1003, "空的信息");
    /**
     * 状态标识
     */
    private final int state;
    /**
     * 状态信息
     */

    private final String stateInfo;

    UserShopMapStateEnum(int state, String stateInfo) {
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
    public static UserShopMapStateEnum getStateEnum(int index) {
        for (UserShopMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
