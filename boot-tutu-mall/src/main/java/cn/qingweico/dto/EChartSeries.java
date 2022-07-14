package cn.qingweico.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/9
 */
@Data
public class EChartSeries {
    private String name;
    private final static String TYPE = "bar";
    private List<Integer> data;
}
