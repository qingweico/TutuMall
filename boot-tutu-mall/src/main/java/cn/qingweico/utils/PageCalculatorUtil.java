package cn.qingweico.utils;

/**
 * @author zqw
 * @date 2020/09/21
 */
public class PageCalculatorUtil {
    public static int calculatorRowIndex(int pageIndex, int pageSize) {
        return pageIndex > 0 ? (pageIndex - 1) * pageSize : 0;
    }
}
