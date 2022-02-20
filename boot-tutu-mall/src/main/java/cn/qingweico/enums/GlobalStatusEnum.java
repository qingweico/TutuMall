package cn.qingweico.enums;

/**
 * 系统全局状态
 *
 * @author:qiming
 * @date: 2021/11/11
 */
public enum GlobalStatusEnum {
    /**
     * 可用
     */
    available("可用", 1),
    disable("不可用", 0);

    String type;
    Integer val;

    GlobalStatusEnum(String type, Integer val) {
        this.type = type;
        this.val = val;
    }
}
