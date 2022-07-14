package cn.qingweico.dto;

import cn.qingweico.entity.HeadLine;
import cn.qingweico.enums.HeadLineStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/11/13
 */
@Data
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
}
