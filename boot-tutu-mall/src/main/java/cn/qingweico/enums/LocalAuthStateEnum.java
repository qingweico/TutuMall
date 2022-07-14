package cn.qingweico.enums;

/**
 * 有关本地账号操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/11/11
 */

public enum LocalAuthStateEnum {
    /**
     * 密码或帐号输入有误
     */
    LOGIN_FAIL(-1, "密码或帐号输入有误"),
    /**
     * 账号绑定成功
     */
    BIND_SUCCESS(200, "帐号绑成功"),
    /**
     * 内部错误
     */
    INNER_ERROR(200, "内部错误"),
    /**
     * 新密码和旧密码不一样
     */
    PASSWORD_DIFFERENT(-1006, "新密码和旧密码不一样"),
    /**
     * 密码修改成功
     */
    PASSWORD_SUCCESS(-1006, "密码修改成功"),
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
