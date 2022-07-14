package cn.qingweico.dto;

import cn.qingweico.entity.Award;
import cn.qingweico.enums.AwardStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/15
 */
@Data
@NoArgsConstructor
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
}
