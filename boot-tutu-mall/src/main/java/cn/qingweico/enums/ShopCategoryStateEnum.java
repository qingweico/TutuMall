package cn.qingweico.enums;

/**
 * 店铺类别操作状态枚举
 * @author zqw
 * @date 2020/11/13
 */

public enum ShopCategoryStateEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 失败
     */
    FAIL(-1001, "失败"),
    /**
     * 区域信息为空
     */
    EMPTY(-1002, "区域信息为空");

    private final int state;

    private final String stateInfo;

    ShopCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ShopCategoryStateEnum getStateEnum(int index) {
        for (ShopCategoryStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
