package cn.qingweico.dto;

import cn.qingweico.entity.ProductCategory;
import cn.qingweico.enums.ProductCategoryStateEnum;
import cn.qingweico.enums.ShopStateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zqw
 * @date 2020/10/03
 */
@Data
public class ProductCategoryExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;
    /**
     * 商品类别列表
     */
    private List<ProductCategory> productCategoryList;

    public ProductCategoryExecution(){}
    /**
     * 对商品类别有关操作失败时的构造器
     *
     * @param productCategoryStateEnum productCategoryStateEnum
     */
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum) {
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
    }

    /**
     * 对商品类别有关操作成功时的构造器
     *
     * @param shopStateEnum       shopStateEnum
     * @param productCategoryList productCategoryList
     */
    public ProductCategoryExecution(ShopStateEnum shopStateEnum, List<ProductCategory> productCategoryList) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }
}
