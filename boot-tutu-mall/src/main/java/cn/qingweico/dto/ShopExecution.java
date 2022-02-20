package cn.qingweico.dto;

import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ShopStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/14
 */
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
     * 店铺数量
     */
    private int count;
    /**
     * 操作的店铺
     */
    private Shop shop;
    /**
     * 店铺列表
     */
    private List<Shop> shopList;

    public ShopExecution() {
    }

    /**
     * 对单个店铺有关操作失败时的构造器
     *
     * @param shopStateEnum shopStateEnum
     */
    public ShopExecution(ShopStateEnum shopStateEnum) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
    }

    /**
     * 对单个店铺有关操作成功时的构造器
     *
     * @param shopStateEnum shopStateEnum
     * @param shop          shop
     */
    public ShopExecution(ShopStateEnum shopStateEnum, Shop shop) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shop = shop;
    }

    /**
     * 对批量店铺操作成功时的构造器
     *
     * @param shopStateEnum shopStateEnum
     * @param shopList      shopList
     */
    public ShopExecution(ShopStateEnum shopStateEnum, List<Shop> shopList) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shopList = shopList;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
