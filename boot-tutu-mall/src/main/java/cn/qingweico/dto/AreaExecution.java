package cn.qingweico.dto;

import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/09/17
 */
@Data
public class AreaExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 区域数量
     */
    private int count;

    /**
     * 操作的区域（增删改区域的时候用）
     */
    private Area area;

    /**
     * 操作的区域（查询区域的时候用）
     */
    private List<Area> areaList;

    /**
     * 操作区域失败时返回的构造器
     *
     * @param stateEnum AreaStateEnum
     */
    public AreaExecution(AreaStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作区域成功时返回的构造器
     *
     * @param stateEnum AreaStateEnum
     * @param area      地区
     */
    public AreaExecution(AreaStateEnum stateEnum, Area area) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.area = area;
    }
}
