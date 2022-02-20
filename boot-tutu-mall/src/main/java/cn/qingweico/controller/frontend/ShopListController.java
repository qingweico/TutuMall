package cn.qingweico.controller.frontend;

import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.service.AreaService;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/03
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/frontend")
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
     * @return JsonResult
     */
    @GetMapping("/listShopsPageInfo")
    public JsonResult listShopsPageInfo(HttpServletRequest request) {
        int parentId = HttpServletRequestUtil.getInteger(request, "parentId");
        HashMap<String, Object> map = new HashMap<>(5);
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1) {
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            } catch (Exception e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (Exception e) {
                map.put("success", false);
            }
        }
        map.put("shopCategoryList", shopCategoryList);
        List<Area> areaList;
        try {
            areaList = areaService.getAreaList();
            map.put("areaList", areaList);
            return JsonResult.ok(map);
        } catch (Exception e) {
            return JsonResult.error();
        }
    }

    /**
     * 组合条件查询店铺
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listShops")
    public JsonResult listShops(HttpServletRequest request) {
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        if (pageIndex > -1 && pageSize > -1) {
            int parentId = HttpServletRequestUtil.getInteger(request, "parentId");
            int shopCategoryId = HttpServletRequestUtil.getInteger(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInteger(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);
            HashMap<String, Object> map = new HashMap<>(5);
            map.put("shopList", shopExecution.getShopList());
            map.put("count", shopExecution.getCount());
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
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
    public Shop compactShopCondition4Search(int parentId, int shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1) {
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
