package cn.qingweico.controller.shop;

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
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周庆伟
 * @date 2020/10/14
 */

@RestController
@RequestMapping("/shop")
public class UserShopManagementController {

    @Resource
    private UserShopMapService userShopMapService;


    /**
     * 获取某个店铺的用户积分信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listUserShopMapsByShop")
    private JsonResult listUserShopMapsByShop(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(2);
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取当前店铺的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            UserShopMap userShopMapCondition = new UserShopMap();
            // 传入查询条件
            userShopMapCondition.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request, "userName");
            if (userName != null) {
                // 若传入顾客名, 则按照顾客名模糊查询
                User customer = new User();
                customer.setName(userName);
                userShopMapCondition.setUser(customer);
            }
            // 分页获取该店铺下的顾客积分列表
            UserShopMapExecution userShopMapExecution = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
            map.put("userShopMapList", userShopMapExecution.getUserShopMapList());
            map.put("count", userShopMapExecution.getCount());
            return JsonResult.ok(map);
        } else {
           return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

}
