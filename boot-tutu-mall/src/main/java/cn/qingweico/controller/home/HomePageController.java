package cn.qingweico.controller.home;

import cn.qingweico.common.Result;
import cn.qingweico.entity.HeadLine;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.service.ShopCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * -------------- 主页信息初始化 --------------
 *
 * @author zqw
 * @date 2020/09/22
 */
@RestControllerAdvice
@RequestMapping("/u")
public class HomePageController {

    @Resource
    ShopCategoryService shopCategoryService;
    @Resource
    HeadLineService headLineService;


    @GetMapping("/initializeHomePageInfo")
    public Result initializeHomePageInfo() {
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
        return Result.ok(result);
    }
}
