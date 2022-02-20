package cn.qingweico.dto;

import cn.qingweico.entity.Product;
import cn.qingweico.enums.ProductStateEnum;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/27
 */
public class ProductExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;
    /**
     * 商品数量
     */
    int count;
    /**
     * 操作的商品
     */
    Product product;
    /**
     * 商品列表
     */
    List<Product> productList;

    public ProductExecution() {
    }

    /**
     * 对商品有关操作失败时的构造器
     *
     * @param productStateEnum productStateEnum
     */
    public ProductExecution(ProductStateEnum productStateEnum) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }

    /**
     * 对单个商品有关操作成功时的构造器
     *
     * @param productStateEnum productStateEnum
     * @param product          商品
     */
    public ProductExecution(ProductStateEnum productStateEnum, Product product) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.product = product;
    }

    /**
     * 对批量商品操作成功时的构造器
     *
     * @param productStateEnum productStateEnum
     * @param productList      商品列表
     */
    public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList = productList;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
