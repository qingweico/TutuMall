package cn.qingweico.dto;

import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ShopStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/09/14
 */
@Data
public class ShopExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;
    /**
     * //
     */
    private Shop shop;

    private List<Shop> shopList;

    public ShopExecution() {
    }

    /**
     * //
     *
     * @param shopStateEnum shopStateEnum
     */
    public ShopExecution(ShopStateEnum shopStateEnum) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
    }

    /**
     * //
     *
     * @param shopStateEnum shopStateEnum
     * @param shop          shop
     */
    public ShopExecution(ShopStateEnum shopStateEnum, Shop shop) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shop = shop;
    }
}
