package cn.qingweico.dto;

import cn.qingweico.entity.ShopAuthRecord;
import cn.qingweico.enums.ShopAuthRecordStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/4
 */
@Data
public class ShopAuthRecordExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 授权数
     */
    private Integer count;

    /**
     * 操作的shopAuthMap
     */
    private ShopAuthRecord shopAuthMap;

    /**
     * 授权列表（查询专用）
     */
    private List<ShopAuthRecord> shopAuthMapList;

    public ShopAuthRecordExecution(){}


    /**
     * 店铺授权失败时返回的构造器
     *
     * @param stateEnum ShopAuthMapStateEnum
     */
    public ShopAuthRecordExecution(ShopAuthRecordStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 店铺授权成功时返回的构造器
     *
     * @param stateEnum   ShopAuthMapStateEnum
     * @param shopAuthMap ShopAuthMap
     */
    public ShopAuthRecordExecution(ShopAuthRecordStateEnum stateEnum, ShopAuthRecord shopAuthMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
    }

    /**
     * 店铺授权成功时返回的构造器(批量)
     *
     * @param stateEnum       ShopAuthMapStateEnum
     * @param shopAuthMapList List<ShopAuthMap>
     */
    public ShopAuthRecordExecution(ShopAuthRecordStateEnum stateEnum, List<ShopAuthRecord> shopAuthMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMapList = shopAuthMapList;
    }
}
