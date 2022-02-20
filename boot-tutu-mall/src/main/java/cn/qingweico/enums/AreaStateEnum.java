package cn.qingweico.enums;

/**
 * 有关区域操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/11/13
 */

public enum AreaStateEnum {
    /**
     * 非法区域
     */
    OFFLINE(-1, "非法区域"),
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * 区域信息为空
     */
    EMPTY(-1002, "区域信息为空");

    private final int state;

    private final String stateInfo;

    AreaStateEnum(int state, String stateInfo) {
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
    public static AreaStateEnum getStateEnum(int index) {
        for (AreaStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
