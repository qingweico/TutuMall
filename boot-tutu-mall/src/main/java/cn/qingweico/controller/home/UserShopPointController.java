package cn.qingweico.controller.home;

import cn.qingweico.common.Result;
import cn.qingweico.dto.UserPointRecordExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.service.UserPointRecordService;
import cn.qingweico.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * -------------- 用户店铺中积分情况 --------------
 *
 * @author zqw
 * @date 2020/11/13
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u/point/")
public class UserShopPointController {
    @Resource
    private UserPointRecordService userShopMapService;

    /**
     * 列出用户的积分情况
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    private Result list(HttpServletRequest request) {

        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取顾客信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if ((page > -1) && (pageSize > -1) && (user != null) && (user.getId() != null)) {
            UserPointRecord userPointRecord = new UserPointRecord();
            userPointRecord.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                // 若传入的店铺id不为空, 则取出该店铺该顾客的积分情况
                Shop shop = new Shop();
                shop.setId(shopId);
                userPointRecord.setShop(shop);
            }
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            if (shopName != null) {
                // 若商品名为非空, 则将其添加进查询条件里进行模糊查询
                Shop shop = new Shop();
                shop.setName(shopName);
                userPointRecord.setShop(shop);
            }
            // 根据查询条件获取顾客的各店铺积分情况
            UserPointRecordExecution ue = userShopMapService.userPointRecordList
                    (userPointRecord, page, pageSize);
            return Result.ok(ue.getUserPointRecordList());
        }
        log.error("page: {}, pageSize: {}, user: {}", page, pageSize, user);
        return Result.error();
    }
}
