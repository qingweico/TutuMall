package cn.qingweico.controller.home;

import cn.qingweico.common.Result;
import cn.qingweico.controller.shop.ShopProductController;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.service.ProductService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * -------------- 主页店铺详情 --------------
 *
 * @author zqw
 * @date 2020/11/15
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u/shop/detail")
public class ShopDetailController {

    @Resource
    ShopService shopService;
    @Resource
    ProductService productService;
    @Resource
    ProductCategoryService productCategoryService;


    /**
     * 获取店铺信息以及店铺下的商品类别列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    public Result listShopDetailPageInfo(HttpServletRequest request) {
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop;
        List<ProductCategory> productCategoryList;
        if (shopId != -1) {
            shop = shopService.getShopById(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            Map<String, Object> map = new HashMap<>(2);
            map.put("shop", shop);
            map.put("productCategoryList", productCategoryList);
            return Result.ok(map);
        }
        log.error("shopId: {}", shopId);
        return Result.error();
    }

    /**
     * 组合条件查询店铺下的商品
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/listProducts")
    public Result listProductByShop(HttpServletRequest request) {
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        if (page > -1 && pageSize > -1 && shopId > -1) {
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = ShopProductController.compactProductCondition(shopId, productCategoryId, productName, false);
            ProductExecution productExecution = productService.getProductList(productCondition, page, pageSize);
            Map<String, Object> map = new HashMap<>(2);
            map.put("productList", productExecution.getProductList());
            map.put("count", productExecution.getCount());
            return Result.ok(map);
        }
        log.error("page: {}, pageSize: {}, shopId: {}", page, pageSize, shopId);
        return Result.error();
    }
}
