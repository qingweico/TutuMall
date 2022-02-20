package cn.qingweico.dto;

import java.util.HashSet;

/**
 * @author 周庆伟
 * @date 2020/10/9
 */

public class EChartXAxis {
    private final static String TYPE = "category";
    /**
     * 去重
     */
    private HashSet<String> data;

    public HashSet<String> getData() {
        return data;
    }

    public void setData(HashSet<String> data) {
        this.data = data;
    }

    public String getType() {
        return TYPE;
    }

}
