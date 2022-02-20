package cn.qingweico.controller.frontend;

import cn.qingweico.controller.shop.ProductManagementController;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.service.ProductService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 周庆伟
 * @date 2020/11/15
 */
@RestControllerAdvice
@RequestMapping("/frontend")
public class ShopDetailController {

    ShopService shopService;

    ProductService productService;

    ProductCategoryService productCategoryService;

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setProductCategoryService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * 获取店铺信息以及店铺下的商品类别列表
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/listShopDetailPageInfo")
    public JsonResult listShopDetailPageInfo(HttpServletRequest request) {
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        Shop shop;
        List<ProductCategory> productCategoryList;
        if (shopId != -1) {
            shop = shopService.getShopById(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            Map<String, Object> map = new HashMap<>(2);
            map.put("shop", shop);
            map.put("productCategoryList", productCategoryList);
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorMsg(ShopStateEnum.NULL_SHOP_ID.getStateInfo());
        }
    }

    /**
     * 组合条件查询店铺下的商品
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listProductsByShop")
    public JsonResult listProductByShop(HttpServletRequest request) {
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        if (pageIndex > -1 && pageSize > -1 && shopId > -1) {
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = ProductManagementController.compactProductCondition(shopId, productCategoryId, productName, false);
            ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>(2);
            map.put("productList", productExecution.getProductList());
            map.put("count", productExecution.getCount());
            return JsonResult.ok(map);
        } else {
            return new JsonResult(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }
}
