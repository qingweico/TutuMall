package cn.qingweico.controller.frontend;

import cn.qingweico.entity.HeadLine;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周庆伟
 * @date 2020/09/22
 */
@RestControllerAdvice
@RequestMapping("/frontend")
public class HomePageController {

    ShopCategoryService shopCategoryService;

    HeadLineService headLineService;

    @Autowired
    public void setShopCategoryService(ShopCategoryService shopCategoryService) {
        this.shopCategoryService = shopCategoryService;
    }

    @Autowired
    public void setHeadLineService(HeadLineService headLineService) {
        this.headLineService = headLineService;
    }

    @GetMapping("/initializeHomePageInfo")
    public JsonResult initializeHomePageInfo() {
        List<ShopCategory> shopCategoryList;
        // 获取一级店铺类别列表(parentId为空的ShopCategory)
        Map<String, Object> result = new HashMap<>(2);
        shopCategoryList = shopCategoryService.getShopCategoryList(null);
        result.put("shopCategoryList", shopCategoryList);

        List<HeadLine> headLineList;
        // 获取状态为可用的头条列表
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        headLineList = headLineService.getHeadLineList(headLine);
        result.put("headLineList", headLineList);
        return JsonResult.ok(result);
    }
}
