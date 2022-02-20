package cn.qingweico.dto;

import cn.qingweico.entity.HeadLine;
import cn.qingweico.enums.HeadLineStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/11/13
 */

public class HeadLineExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 头条数量
     */
    private int count;

    /**
     * 操作的headLine（增删改headLine的时候用）
     */
    private HeadLine headLine;

    /**
     * 获取的头条列表(查询头条列表的时候用)
     */
    private List<HeadLine> headLineList;

    public HeadLineExecution() {
    }

    /**
     * 操作头条失败时返回的构造器
     *
     * @param stateEnum HeadLineStateEnum
     */
    public HeadLineExecution(HeadLineStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作头条成功时返回的构造器
     *
     * @param stateEnum HeadLineStateEnum
     * @param headLine  headLine
     */
    public HeadLineExecution(HeadLineStateEnum stateEnum, HeadLine headLine) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.headLine = headLine;
    }

    /**
     * 批量操作头条成功时返回的构造器
     *
     * @param stateEnum    HeadLineStateEnum
     * @param headLineList List<HeadLine>
     */
    public HeadLineExecution(HeadLineStateEnum stateEnum, List<HeadLine> headLineList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.headLineList = headLineList;
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

    public HeadLine getHeadLine() {
        return headLine;
    }

    public void setHeadLine(HeadLine headLine) {
        this.headLine = headLine;
    }

    public List<HeadLine> getHeadLineList() {
        return headLineList;
    }

    public void setHeadLineList(List<HeadLine> headLineList) {
        this.headLineList = headLineList;
    }

}
