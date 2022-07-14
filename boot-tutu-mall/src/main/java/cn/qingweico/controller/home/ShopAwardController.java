package cn.qingweico.controller.home;


import cn.qingweico.common.Result;
import cn.qingweico.dto.AwardExecution;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.service.AwardService;
import cn.qingweico.service.UserPointRecordMapService;
import cn.qingweico.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * -------------- 主页店铺奖品 --------------
 *
 * @author zqw
 * @date 2020/10/15
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u/award")
public class ShopAwardController {

    @Resource
    private AwardService awardService;
    @Resource
    private UserPointRecordMapService userShopMapService;


    /**
     * 列出店铺设定的奖品列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    private Result listAwardsByShop(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 获取店铺Id
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        // 空值判断
        if ((page > -1) && (pageSize > -1) && (shopId > -1)) {
            // 获取前端可能输入的奖品名模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition4Search(shopId, awardName);
            // 传入查询条件分页获取奖品信息
            AwardExecution awardExecution = awardService.getAwardList(awardCondition, page, pageSize);
            Map<String, Object> map = new HashMap<>(3);
            map.put("awardList", awardExecution.getAwardList());
            map.put("count", awardExecution.getCount());
            // 显示该用户在本店铺的积分
            User user = (User) request.getSession().getAttribute("user");
            // 空值判断
            if (user != null && user.getId() != null) {
                // 获取该用户在本店铺的积分信息
                UserPointRecord userShopMap = userShopMapService.getUserPointRecord(user.getId(), shopId);
                if (userShopMap == null) {
                    map.put("totalPoint", 0);
                } else {
                    map.put("totalPoint", userShopMap.getPoint());
                }
            }
            return Result.ok(map);
        }
        log.error("page: {}, pageSize: {}, shopId: {}", page, pageSize, shopId);
        return Result.error();
    }

    /**
     * 封装查询条件
     *
     * @param shopId    店铺id
     * @param awardName 奖品名称
     * @return Award
     */
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        // 只取出可用的奖品
        awardCondition.setEnableStatus(1);
        if (awardName != null) {
            awardCondition.setName(awardName);
        }
        return awardCondition;
    }
}
