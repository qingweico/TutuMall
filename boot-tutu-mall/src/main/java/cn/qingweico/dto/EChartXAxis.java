package cn.qingweico.dto;

import lombok.Data;

import java.util.HashSet;

/**
 * @author zqw
 * @date 2020/10/9
 */
@Data
public class EChartXAxis {
    private final static String TYPE = "category";
    /**
     * 去重
     */
    private HashSet<String> data;
}
