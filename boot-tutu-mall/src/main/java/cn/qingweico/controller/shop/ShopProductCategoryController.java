package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ProductCategoryExecution;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ProductCategoryStateEnum;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * -------------- 店铺商品类别 --------------
 *
 * @author zqw
 * @date 2020/10/16
 */
@Slf4j
@RestController
@RequestMapping("/shop/productCategory")
public class ShopProductCategoryController {

    @Resource
    ProductCategoryService productCategoryService;

    /**
     * 获取商品类别列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    public Result getProductCategoryList(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list;
        if (currentShop != null && currentShop.getId() > 0) {
            list = productCategoryService.getProductCategoryList(currentShop.getId());
            return Result.ok(list);
        }
        log.error("currentShop: {}", currentShop);
        return Result.error();
    }

    /**
     * 添加商品类别
     *
     * @param productCategoryList 一个或者多个商品类别
     * @param request             HttpServletRequest
     * @return Result
     */
    @PostMapping("/add")
    public Result addProductCategory(@RequestBody List<ProductCategory> productCategoryList,
                                     HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getId());
        }
        if (productCategoryList.size() > 0) {
            ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                return Result.ok();
            } else {
                String msg = productCategoryExecution.getStateInfo();
                return Result.errorMsg(msg);
            }
        } else {
            log.error("currentShop: {}, productCategoryList: {}", currentShop, productCategoryList);
            return Result.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 删除商品类别
     *
     * @param productCategoryId 商品类别id
     * @param request           HttpServletRequest
     * @return Result
     */
    @PostMapping("/remove")
    public Result removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        if (productCategoryId != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            ProductCategoryExecution productCategoryExecution =
                    productCategoryService.deleteProductCategoryById(productCategoryId,
                            currentShop.getId());
            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                return Result.ok();
            } else {
                String msg = productCategoryExecution.getStateInfo();
                return Result.errorMsg(msg);
            }
        } else {
            log.error("productCategoryId: {}", productCategoryId);
            return Result.error();
        }
    }
}
