package cn.qingweico.dto;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/9
 */

public class EChartSeries {
    private String name;
    private final static String TYPE = "bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getType() {
        return TYPE;
    }

}
