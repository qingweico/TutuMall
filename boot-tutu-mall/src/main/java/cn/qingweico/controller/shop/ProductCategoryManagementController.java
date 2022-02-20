package cn.qingweico.controller.shop;

import cn.qingweico.dto.ProductCategoryExecution;
import cn.qingweico.dto.Result;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ProductCategoryStateEnum;
import cn.qingweico.exception.ProductCategoryOperationException;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/16
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ProductCategoryManagementController {

    @Resource
    ProductCategoryService productCategoryService;

    /**
     * 获取商品类别列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/getProductCategoryList")
    public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list;
        if (currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<>(true, list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<>(false, ps.getState(), ps.getStateInfo());
        }
    }

    /**
     * 添加商品类别
     *
     * @param productCategoryList 一个或者多个商品类别
     * @param request             HttpServletRequest
     * @return JsonResult
     */
    @PostMapping("/addProductCategory")
    public JsonResult addProductCategory(@RequestBody List<ProductCategory> productCategoryList,
                                         HttpServletRequest request) {

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }
        if (productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok();
                } else {
                    String msg = productCategoryExecution.getStateInfo();
                    return JsonResult.errorMsg(msg);
                }
            } catch (ProductCategoryOperationException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 删除商品类别
     *
     * @param productCategoryId 商品类别id
     * @param request           HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/removeProductCategory")
    public JsonResult removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution =
                        productCategoryService.deleteProductCategoryById(productCategoryId,
                                currentShop.getShopId());
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok();
                } else {
                    String msg = productCategoryExecution.getStateInfo();
                    return JsonResult.errorMsg(msg);
                }
            } catch (ProductCategoryOperationException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }
}
