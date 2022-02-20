package cn.qingweico.controller.frontend;

import cn.qingweico.dto.UserShopMapExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserShopMap;
import cn.qingweico.service.UserShopMapService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周庆伟
 * @date 2020/11/13
 */

@RestControllerAdvice
@RequestMapping("/frontend")
public class UserShopPointController {
    @Resource
    private UserShopMapService userShopMapService;

    /**
     * 列出用户的积分情况
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/listUserShopMapsByCustomer")
    private JsonResult listUserShopMapsByCustomer(HttpServletRequest request) {

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取顾客信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setUser(user);
            int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
            if (shopId > -1) {
                // 若传入的店铺id不为空, 则取出该店铺该顾客的积分情况
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userShopMapCondition.setShop(shop);
            }
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            if (shopName != null) {
                // 若商品名为非空, 则将其添加进查询条件里进行模糊查询
                Shop shop = new Shop();
                shop.setShopName(shopName);
                userShopMapCondition.setShop(shop);
            }
            // 根据查询条件获取顾客的各店铺积分情况
            UserShopMapExecution ue = userShopMapService.listUserShopMap
                    (userShopMapCondition, pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("userShopMapList", ue.getUserShopMapList());
            map.put("count", ue.getCount());
            map.put("success", true);
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }
}
