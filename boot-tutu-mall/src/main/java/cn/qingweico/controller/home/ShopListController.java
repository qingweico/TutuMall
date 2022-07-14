package cn.qingweico.controller.home;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.service.AreaService;
import cn.qingweico.service.ShopCategoryService;
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

/**
 * -------------- 主页店铺列表 --------------
 *
 * @author zqw
 * @date 2020/10/03
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u/shop")
public class ShopListController {

    @Resource
    AreaService areaService;
    @Resource
    ShopCategoryService shopCategoryService;
    @Resource
    ShopService shopService;

    /**
     * 店铺列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    public Result listShopsPageInfo(HttpServletRequest request) {
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        HashMap<String, Object> map = new HashMap<>(2);
        List<ShopCategory> shopCategoryList;
        if (parentId != -1) {
            ShopCategory shopCategoryCondition = new ShopCategory();
            ShopCategory parent = new ShopCategory();
            parent.setId(parentId);
            shopCategoryCondition.setParent(parent);
            shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        } else {
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
        }
        map.put("shopCategoryList", shopCategoryList);
        List<Area> areaList;
        areaList = areaService.getAreaList();
        map.put("areaList", areaList);
        return Result.ok(map);
    }

    /**
     * 组合条件查询店铺
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listShops")
    public Result listShops(HttpServletRequest request) {
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        if (page > -1 && pageSize > -1) {
            int parentId = HttpServletRequestUtil.getInteger(request, "parentId");
            int shopCategoryId = HttpServletRequestUtil.getInteger(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInteger(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            Shop shopCondition = compactShopCondition(parentId, shopCategoryId, areaId, shopName);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, page, pageSize);
            return Result.ok(shopExecution.getShopList());
        }
        log.error("page: {}, pageSize: {}", page, pageSize);
        return Result.error();
    }

    /**
     * 组合条件查询店铺下的商品
     *
     * @param parentId       所属一级店铺id
     * @param shopCategoryId 店铺类别id
     * @param areaId         区域id
     * @param shopName       店铺名称
     * @return Shop
     */
    public Shop compactShopCondition(long parentId, long shopCategoryId, long areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1) {
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1) {
            shopCondition.setAreaId(areaId);
        }
        if (shopName != null) {
            shopCondition.setName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
