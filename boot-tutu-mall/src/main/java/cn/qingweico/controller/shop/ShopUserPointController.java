package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.UserPointRecordExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserPointRecord;
import cn.qingweico.service.UserPointRecordService;
import cn.qingweico.utils.HttpServletRequestUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zqw
 * @date 2020/10/14
 */

@RestController
@RequestMapping("/shop/u/point")
public class ShopUserPointController {

    @Resource
    private UserPointRecordService userShopMapService;


    /**
     * 获取某个店铺的用户积分信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/list")
    private Result listUserPointByShop(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取当前店铺的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((page > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getId() != null)) {
            UserPointRecord userPointRecord = new UserPointRecord();
            // 传入查询条件
            userPointRecord.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request, "userName");
            if (userName != null) {
                // 若传入顾客名, 则按照顾客名模糊查询
                User customer = new User();
                customer.setName(userName);
                userPointRecord.setUser(customer);
            }
            // 分页获取该店铺下的用户积分列表
            UserPointRecordExecution upre = userShopMapService.userPointRecordList(userPointRecord, page, pageSize);
            return Result.ok(upre.getUserPointRecordList());
        }
        return Result.error();
    }

}
