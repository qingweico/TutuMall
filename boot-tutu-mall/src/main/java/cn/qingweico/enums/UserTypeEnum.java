package cn.qingweico.enums;

/**
 * 有关平台用户账户类型枚举
 *
 * @author:qiming
 * @date: 2021/11/21
 */
public enum UserTypeEnum {

    /**
     * 顾客
     */
    CUSTOMER(1, "顾客"),
    /**
     * 店家
     */
    OWNER(2, "店家"),
    /**
     * 管理员
     */
    ADMIN(3, "管理员");
    private final int type;

    private final String desc;


    UserTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
