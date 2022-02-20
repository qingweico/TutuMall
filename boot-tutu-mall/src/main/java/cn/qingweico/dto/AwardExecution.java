package cn.qingweico.dto;

import cn.qingweico.entity.Award;
import cn.qingweico.enums.AwardStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

public class AwardExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 店铺数量
     */
    private int count;

    /**
     * 操作的award（增删改商品的时候用）
     */
    private Award award;

    /**
     * 获取的award列表(查询商品列表的时候用)
     */
    private List<Award> awardList;

    public AwardExecution() {
    }

    /**
     * 操作失败时返回的构造器
     *
     * @param stateEnum AwardStateEnum
     */
    public AwardExecution(AwardStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功时返回的构造器
     *
     * @param stateEnum AwardStateEnum
     * @param award     Award
     */
    public AwardExecution(AwardStateEnum stateEnum, Award award) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.award = award;
    }

    /**
     * 批量操作成功时返回的构造器
     *
     * @param stateEnum AwardStateEnum
     * @param awardList List<Award>
     */
    public AwardExecution(AwardStateEnum stateEnum, List<Award> awardList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.awardList = awardList;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

}
