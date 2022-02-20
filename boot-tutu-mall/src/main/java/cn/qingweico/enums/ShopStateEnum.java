package cn.qingweico.enums;

/**
 * 有关店铺操作状态枚举
 * @author 周庆伟
 * @date 2020/09/15
 */

public enum ShopStateEnum {
    /**
     * 审核中
     */
    CHECK(0, "审核中"),
    /**
     * 非法店铺
     */
    OFFLINE(-1, "非法店铺"),
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 通过审核
     */
    PASS(2, "通过审核"),
    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部错误"),
    /**
     * SHOP_ID为空
     */
    NULL_SHOP_ID(-1002, "SHOP_ID为空"),
    /**
     * 店铺信息为空
     */
    NULL_SHOP(-1003, "店铺信息为空"),
    /**
     * 店铺名称为空
     */
    NULL_SHOP_NAME(-1004, "店铺名称为空");

    /**
     * 店铺的状态
     */
    private int state;
    /**
     * 店铺的状态信息
     */
    private String stateInfo;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static ShopStateEnum getStateEnum(int state) {
        for (ShopStateEnum shopStateEnum : values()) {
            if (shopStateEnum.getState() == state) {
                return shopStateEnum;
            }
        }
        return null;
    }
}
