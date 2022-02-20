package cn.qingweico.controller.admin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.qingweico.dao.AreaDao;
import cn.qingweico.dao.ShopDao;
import cn.qingweico.dto.Constant4SuperAdmin;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */

@RestController
@RequestMapping("/superadmin")
public class ShopController {
    private AreaDao areaDao;

    private ShopDao shopDao;

    private ShopService shopService;

    private ShopCategoryService shopCategoryService;

    private final Map<String, Object> map = new HashMap<>(5);

    @Autowired
    public void setAreaDao(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Autowired
    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Autowired
    public void setShopCategoryService(ShopCategoryService shopCategoryService) {
        this.shopCategoryService = shopCategoryService;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    /**
     * 获取店铺列表
     *
     * @param pageIndex 起始查询的页数
     * @param pageSize  每页的数量
     * @return Map
     */
    @GetMapping("/listshops/{pageIndex}/{pageSize}")
    private Map<String, Object> listShops(@PathVariable("pageIndex") Integer pageIndex, @PathVariable("pageSize") Integer pageSize) {
        // 获取分页信息
        // 空值判断List<Shop> queryAllShop
        List<Shop> shopList;
        if (pageIndex > 0 && pageSize > 0) {
            try {
                pageIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
                shopList = shopDao.queryAllShop(pageIndex, pageSize);
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }
            if (shopList != null) {
                map.put(Constant4SuperAdmin.PAGE_SIZE, shopList);
                map.put(Constant4SuperAdmin.TOTAL, shopList.size());
                map.put("success", true);
            } else {
                // 取出数据为空,也返回new list以使得前端不出错
                map.put(Constant4SuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
                map.put(Constant4SuperAdmin.TOTAL, 0);
                map.put("success", true);
            }
        } else {
            map.put("success", false);
            map.put("errorMessage", "请检查您的查询信息!");
        }
        return map;
    }

    /**
     * 根据id返回店铺信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/searchshopbyid")
    private Map<String, Object> searchShopById(HttpServletRequest request) {
        Shop shop;
        // 从请求中获取店铺Id
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        if (shopId > 0) {
            try {
                // 根据Id获取店铺实例
                shop = shopService.getShopById(shopId);
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }
            if (shop != null) {
                List<Shop> shopList = new ArrayList<>();
                shopList.add(shop);
                map.put(Constant4SuperAdmin.PAGE_SIZE, shopList);
                map.put(Constant4SuperAdmin.TOTAL, 1);
            } else {
                map.put(Constant4SuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
                map.put(Constant4SuperAdmin.TOTAL, 0);
            }
            map.put("success", true);
        } else {
            map.put("success", false);
            map.put("errorMessage", "空的查询信息");
        }
        return map;
    }

    /**
     * 修改店铺信息, 主要修改可用状态, 审核用
     *
     * @return Map
     */
    @PostMapping("/modifyShop")
    private Map<String, Object> modifyShop(@RequestBody Map<String, Object> params) {
        ObjectMapper mapper = new ObjectMapper();
        String shopString = (String) params.get("shopString");
        Shop shop;

        try {
            // 获取前端传递过来的shop json字符串, 将其转换成shop实例
            shop = mapper.readValue(shopString, Shop.class);
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }
        // 空值判断
        if (shop != null && shop.getShopId() != null) {
            try {
                // 若前端传来需要修改的字段,则设置上, 否则设置为空, 为空则不修改
                shop.setShopName(
                        (shop.getShopName() == null) ? null : (URLDecoder.decode(shop.getShopName(), "UTF-8")));
                shop.setShopDescription(
                        (shop.getShopDescription() == null) ? null : (URLDecoder.decode(shop.getShopDescription(), "UTF-8")));
                shop.setShopAddress(
                        (shop.getShopAddress() == null) ? null : (URLDecoder.decode(shop.getShopAddress(), "UTF-8")));
                if (shop.getShopCategory() != null && shop.getShopCategory().getShopCategoryId() != null) {
                    // 若需要修改店铺类别,则先获取店铺类别
                    ShopCategory sc = shopCategoryService
                            .getShopCategoryById(shop.getShopCategory().getShopCategoryId());
                    shop.setShopCategory(sc);
                }
                // 修改店铺信息
                ShopExecution ae = shopService.modifyShop(shop, null);
                if (ae.getState() == ShopStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入店铺信息");
        }
        return map;
    }

    /**
     * 获取所有区域信息
     *
     * @return List<Area>
     */
    @GetMapping("/getallarea")
    public List<Area> getAllArea() {
        return areaDao.queryAllArea();
    }

    /**
     * 根据店铺id更新店铺信息
     *
     * @param shopId  店铺id
     * @param request request
     * @return model
     */
    @GetMapping("/updateshopstatus/{shopId}")
    public Map<String, Object> updateShopStatus(@PathVariable("shopId") Integer shopId, HttpServletRequest request) {
        if (shopId == null) {
            map.put("success", false);
            map.put("errorMessage", "请传入店铺id!");
        } else {
            boolean enableStatus = HttpServletRequestUtil.getBoolean(request, "enableStatus");
            int enable = enableStatus ? 1 : 0;
            Shop shop = new Shop();
            shop.setShopId(shopId);
            shop.setEnableStatus(enable);
            ShopExecution shopExecution = shopService.updateShopStatus(shop);
            if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errorMessage", shopExecution.getStateInfo());
            }
        }
        return map;

    }

}
