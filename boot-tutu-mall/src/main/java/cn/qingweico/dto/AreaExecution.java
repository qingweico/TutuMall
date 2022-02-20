package cn.qingweico.dto;

import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/17
 */

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


    public AreaExecution() {
    }

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

    /**
     * 批量操作区域成功时返回的构造器
     *
     * @param stateEnum AreaStateEnum
     * @param areaList  区域列表
     */
    public AreaExecution(AreaStateEnum stateEnum, List<Area> areaList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.areaList = areaList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

}
