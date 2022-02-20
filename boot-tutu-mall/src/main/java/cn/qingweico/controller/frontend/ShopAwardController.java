package cn.qingweico.controller.frontend;


import cn.qingweico.dto.AwardExecution;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserShopMap;
import cn.qingweico.service.AwardService;
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
 * @date 2020/10/15
 */
@RestControllerAdvice
@RequestMapping("/frontend")
public class ShopAwardController {

    @Resource
    private AwardService awardService;
    @Resource
    private UserShopMapService userShopMapService;


    /**
     * 列出店铺设定的奖品列表
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listAwardsByShop")
    private JsonResult listAwardsByShop(HttpServletRequest request) {
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 获取店铺Id
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
            // 获取前端可能输入的奖品名模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition4Search(shopId, awardName);
            // 传入查询条件分页获取奖品信息
            AwardExecution awardExecution = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>(10);
            map.put("awardList", awardExecution.getAwardList());
            map.put("count", awardExecution.getCount());
            // 从Session中获取用户信息, 主要是为了显示该用户在本店铺的积分
            User user = (User) request.getSession().getAttribute("user");
            // 空值判断
            if (user != null && user.getUserId() != null) {
                // 获取该用户在本店铺的积分信息
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
                if (userShopMap == null) {
                    map.put("totalPoint", 0);
                } else {
                    map.put("totalPoint", userShopMap.getPoint());
                }
            }
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 封装查询条件
     *
     * @param shopId    店铺id
     * @param awardName 奖品名称
     * @return Award
     */
    private Award compactAwardCondition4Search(int shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        // 只取出可用的奖品
        awardCondition.setEnableStatus(1);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
}
