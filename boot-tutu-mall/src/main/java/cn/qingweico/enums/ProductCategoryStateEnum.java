package cn.qingweico.enums;

/**
 * 有关商品类别操作状态枚举
 *
 * @author 周庆伟
 * @date 2020/09/21
 */

public enum ProductCategoryStateEnum {
    /**
     *
     */
    SUCCESS(200, "操作成功"),
    /**
     *
     */
    INNER_ERROR(-1001, "操作失败"),
    /**
     *
     */
    EMPTY_LIST(-1002, "添加数少于1");

    private final int state;

    private final String stateInfo;

    ProductCategoryStateEnum(int state, String stateInfo) {
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
    public static ProductCategoryStateEnum getStateEnum(int state) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == state) {
                return productCategoryStateEnum;
            }
        }
        return null;
    }
}
