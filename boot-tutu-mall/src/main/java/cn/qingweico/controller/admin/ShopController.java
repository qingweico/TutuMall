package cn.qingweico.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.qingweico.common.Result;
import cn.qingweico.dao.AreaDao;
import cn.qingweico.dao.ShopDao;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zqw
 * @date 2020/11/14
 */
@Slf4j
@RestController
@RequestMapping("/a/shop")
public class ShopController {
    @Resource
    private AreaDao areaDao;
    @Resource
    private ShopDao shopDao;
    @Resource
    private ShopService shopService;
    @Resource
    private ShopCategoryService shopCategoryService;


    /**
     * 获取店铺列表
     *
     * @param page     起始查询的页数
     * @param pageSize 每页的数量
     * @return Result
     */
    @GetMapping("/list")
    private Result listShops(@RequestParam Integer page, @RequestParam Integer pageSize) {
        List<Shop> shopList;
        if (page > 0 && pageSize > 0) {
            page = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
            shopList = shopDao.queryAllShop(page, pageSize);

            if (shopList != null) {
                return Result.ok(shopList);
            } else {
                return Result.ok(new ArrayList<>());
            }
        }
        return Result.error();
    }

    /**
     * 根据id返回店铺信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("get")
    private Result searchShopById(HttpServletRequest request) {
        Shop shop;
        // 从请求中获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > 0) {
            shop = shopService.getShopById(shopId);
            if (shop != null) {
                return Result.ok(shop);
            } else {
                log.error("不存在店铺id为 [{}] 的店铺!", shopId);
            }
        }
        log.error("shop id 获取失败,请检查!");
        return Result.ok(new ArrayList<>());
    }

    /**
     * 修改店铺信息, 主要修改可用状态, 审核用
     *
     * @return Result
     */
    @PostMapping("/modify")
    private Result modifyShop(@RequestBody Shop shop) {
        // 空值判断
        if (shop != null && shop.getId() != null) {
            if (shop.getShopCategory() != null && shop.getShopCategory().getId() != null) {
                // 若需要修改店铺类别,则先获取店铺类别
                ShopCategory sc = shopCategoryService.getShopCategoryById(shop.getShopCategory().getId());
                shop.setShopCategory(sc);
            }
            // 修改店铺信息
            ShopExecution ae = shopService.modifyShop(shop, null);
            if (ae.getState() == ShopStateEnum.SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        }
        log.error("shop获取失败,请检查!");
        return Result.error();
    }

    /**
     * 获取所有区域信息
     *
     * @return Result
     */
    @GetMapping("/getAllArea")
    public Result getAllArea() {
        return Result.ok(areaDao.queryAllArea());
    }

    /**
     * 根据店铺id更新店铺信息
     *
     * @param shopId  店铺id
     * @param request request
     * @return model
     */
    @PostMapping("/update")
    public Result updateShopStatus(@RequestParam Long shopId, HttpServletRequest request) {
        boolean enableStatus = HttpServletRequestUtil.getBoolean(request, "enableStatus");
        int enable = enableStatus ? 1 : 0;
        Shop shop = new Shop();
        shop.setId(shopId);
        shop.setEnableStatus(enable);
        ShopExecution shopExecution = shopService.updateShopStatus(shop);
        if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
            return Result.errorCustom(ResponseStatusEnum.UPDATE_SUCCESS);
        } else {
            return Result.errorMsg(shopExecution.getStateInfo());
        }
    }
}
