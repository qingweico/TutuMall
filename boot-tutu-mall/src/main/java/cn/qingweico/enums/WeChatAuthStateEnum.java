package cn.qingweico.enums;

/**
 * 有关微信绑定操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/10/21
 */


public enum WeChatAuthStateEnum {
    /**
     * openid输入有误
     */
    LOGIN_FAIL(-1, "openId输入有误"),
    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 注册信息为空
     */
    NULL_AUTH_INFO(-1006, "注册信息为空");

    private final int state;

    private final String stateInfo;

    WeChatAuthStateEnum(int state, String stateInfo) {
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
    public static WeChatAuthStateEnum getStateEnum(int index) {
        for (WeChatAuthStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
