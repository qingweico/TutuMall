package cn.qingweico.enums;

/**
 * 有关本地账号操作状态枚举
 * @author 周庆伟
 * @date 2020/11/11
 */

public enum LocalAuthStateEnum {
    /**
     * 密码或帐号输入有误
     */
    LOGIN_FAIL(-1, "密码或帐号输入有误"),
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 注册信息为空
     */
    NULL_AUTH_INFO(-1006, "注册信息为空"),
    /**
     * 最多只能绑定一个本地帐号
     */
    ONLY_ONE_ACCOUNT(-1007, "最多只能绑定一个本地帐号");

    private final int state;

    private final String stateInfo;

    LocalAuthStateEnum(int state, String stateInfo) {
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
    public static LocalAuthStateEnum getStateEnum(int index) {
        for (LocalAuthStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
