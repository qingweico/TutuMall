package cn.qingweico.dto;

import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopCategoryStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/11/13
 */
@Data
public class ShopCategoryExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private ShopCategory shopCategory;

    private List<ShopCategory> shopCategoryList;

    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum, ShopCategory shopCategory) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopCategory = shopCategory;
    }

    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum, List<ShopCategory> shopCategoryList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopCategoryList = shopCategoryList;
    }
}
