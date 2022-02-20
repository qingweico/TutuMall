package cn.qingweico.enums;

/**
 * 有关头条操作状态枚举
 * @author 周庆伟
 * @date 2020/11/13
 */

public enum HeadLineStateEnum {
    /**
     * 创建成功
     */
    SUCCESS(200, "创建成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     * 头条信息为空
     */
    EMPTY(-1002, "头条信息为空");

    private final int state;

    private final String stateInfo;

    HeadLineStateEnum(int state, String stateInfo) {
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
    public static HeadLineStateEnum getSateEnum(int index) {
        for (HeadLineStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
