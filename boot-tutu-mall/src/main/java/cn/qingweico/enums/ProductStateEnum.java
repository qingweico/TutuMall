package cn.qingweico.enums;

/**
 * 有关商品操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/09/22
 */
public enum ProductStateEnum {
    /**
     * 下架
     */
    OWN(0, "下架"),
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 操作失败
     */
    INNER_ERROR(-1001, "内部错误");
    /**
     * 商品状态
     */
    private int state;
    /**
     * 商品状态标识
     */
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
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
    public static ProductStateEnum getStateEnum(int index) {
        for (ProductStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
