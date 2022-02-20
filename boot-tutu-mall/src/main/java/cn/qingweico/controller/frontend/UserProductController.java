package cn.qingweico.controller.frontend;


import cn.qingweico.dto.UserProductMapExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserProductMap;
import cn.qingweico.service.UserProductMapService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author 周庆伟
 * @date 2020/11/13
 */


@RestControllerAdvice
@RequestMapping("/frontend")
public class UserProductController {

    @Resource
    private UserProductMapService userProductMapService;

    /**
     * 列出顾客的商品消费信息
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/listUserProductMapsByCustomer")
    private JsonResult listUserProductMapsByCustomer(HttpServletRequest request) {
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session里获取顾客信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != -1)) {
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setUser(user);
            int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
            if (shopId > -1) {
                // 若传入店铺信息，则列出某个店铺下该顾客的消费历史
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userProductMapCondition.setShop(shop);
            }
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                // 若传入的商品名不为空，则按照商品名模糊查询
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            // 根据查询条件分页返回用户消费信息
            UserProductMapExecution ue = userProductMapService.listUserProductMap
                    (userProductMapCondition, pageIndex, pageSize);
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("userProductMapList", ue.getUserProductMapList());
            map.put("count", ue.getCount());
            map.put("success", true);
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }
}
