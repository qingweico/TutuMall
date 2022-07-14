package cn.qingweico.controller.frontend;


import cn.qingweico.common.Result;
import cn.qingweico.dto.UserConsumptionRecordExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserConsumptionRecord;
import cn.qingweico.service.UserConsumptionRecordService;
import cn.qingweico.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * -------------- 用户消费记录 --------------
 *
 * @author zqw
 * @date 2020/11/13
 */

@Slf4j
@RestControllerAdvice
@RequestMapping("/u/consumption")
public class UserConsumptionRecordController {

    @Resource
    private UserConsumptionRecordService userProductMapService;

    /**
     * 列出顾客的商品消费信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    private Result list(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session里获取顾客信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if ((page > -1) && (pageSize > -1) && (user != null) && (user.getId() != -1)) {
            UserConsumptionRecord userProductMapCondition = new UserConsumptionRecord();
            userProductMapCondition.setUserId(user.getId());
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                // 若传入店铺信息, 则列出某个店铺下该顾客的消费历史
                userProductMapCondition.setShopId(shopId);
            }
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                // 若传入的商品名不为空，则按照商品名模糊查询
                Product product = new Product();
                product.setName(productName);
                userProductMapCondition.setProduct(product);
            }
            // 根据查询条件分页返回用户消费信息
            UserConsumptionRecordExecution ue = userProductMapService.userConsumptionRecordList
                    (userProductMapCondition, page, pageSize);
            return Result.ok(ue.getUserConsumptionRecordList());
        }
        log.error("page: {}, pageSize: {}, user: {}", page, pageSize, user);
        return Result.error();
    }
}
