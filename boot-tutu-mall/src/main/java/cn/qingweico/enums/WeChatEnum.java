package cn.qingweico.enums;

/**
 * 使用微信扫码的后的状态值
 *
 * @author 周庆伟
 * @date 2021/5/10
 */
public enum WeChatEnum {
    /**
     * 购买商品成功
     */
    PURCHASE_SUCCESS(1, "购买商品成功"),
    /**
     * 购买商品失败
     */
    PURCHASE_FAIL(-1, "购买商品失败"),
    /**
     * 兑换奖品成功
     */
    EXCHANGE_SUCCESS(1001, "兑换奖品成功"),
    /**
     * 兑换奖品失败
     */
    EXCHANGE_FAIL(-1001, "兑换奖品失败"),
    /**
     * 二维码已过期
     */
    EXPIRE(-1002, "二维码已过期"),
    /**
     * 未授权
     */
    UNAUTHORIZED(-1003, "未授权"),
    /**
     * 已授权
     */
    BE_AUTHORIZED(1003, "已授权"),
    /**
     * 授权成功
     */
    AUTHORIZATION_SUCCESS(1004, "授权成功"),
    /**
     * 授权失败
     */
    AUTHORIZATION_FAIL(-1004, "授权失败"),
    /**
     * 出错了
     */
    INNER_ERROR(-1005,"出错了");
    /**
     * 状态标识
     */
    private final int state;
    /**
     * 状态信息
     */
    private final String stateInfo;

    WeChatEnum(int state, String stateInfo) {
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
    public static WeChatEnum getStateEnum(int index) {
        for (WeChatEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
