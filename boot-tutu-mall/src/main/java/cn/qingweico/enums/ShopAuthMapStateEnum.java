package cn.qingweico.enums;

/**
 * 有关店铺授权操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/4
 */

public enum ShopAuthMapStateEnum {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * ShopAuthId为空
     */
    NULL_SHOP_AUTH_ID(-1002, "ShopAuthId为空"),
    /**
     * 授权空的信息
     */
    NULL_SHOP_AUTH_INFO(-1003, "授权空的信息"),

    /**
     * token为空
     */
    NULL_TOKEN(-1004, "token为空");

    private final int state;

    private final String stateInfo;

    ShopAuthMapStateEnum(int state, String stateInfo) {
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
    public static ShopAuthMapStateEnum getStateEnum(int index) {
        for (ShopAuthMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
