package cn.qingweico.controller.shop;

import cn.qingweico.dto.ShopAuthMapExecution;
import cn.qingweico.dto.UserAwardMapExecution;
import cn.qingweico.entity.*;
import cn.qingweico.enums.UserAwardMapStateEnum;
import cn.qingweico.enums.WeChatEnum;
import cn.qingweico.service.ShopAuthMapService;
import cn.qingweico.service.UserAwardMapService;
import cn.qingweico.service.UserService;
import cn.qingweico.service.WeChatAuthService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Objects;

import static cn.qingweico.controller.shop.ShopAuthManagementController.getWeChatAuth;


/**
 * @author 周庆伟
 * @date 2020/10/15
 */

@RestController
@RequestMapping("/shop")
public class UserAwardManagementController {

    @Resource
    UserAwardMapService userAwardMapService;
    @Resource
    UserService userService;
    @Resource
    ShopAuthMapService shopAuthMapService;
    @Resource
    WeChatAuthService weChatAuthService;

    /**
     * 领取奖品二维码有效期限
     */
    private final static Integer TIME_LIMIT = 600000;


    /**
     * 列出某个店铺的用户奖品领取情况列表
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listUserAwardMapsByShop")
    private JsonResult listUserAwardMapsByShop(HttpServletRequest request) {
        // 从session里获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        HashMap<String, Object> map = new HashMap<>(2);
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1)
                && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(currentShop);
            // 从请求中获取奖品名
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 如果需要按照奖品名称搜索, 则添加搜索条件
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            // 分页返回结果
            UserAwardMapExecution ue = userAwardMapService.listReceivedUserAwardMap(userAwardMap, pageIndex, pageSize);
            map.put("userAwardMapList", ue.getUserAwardMapList());
            map.put("count", ue.getCount());
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 操作员扫顾客的奖品二维码并派发奖品, 证明顾客已领取过
     *
     * @param request HttpServletRequest
     * @return String
     * @throws IOException IOException
     */
    @GetMapping("/exchangeAward")
    private String exchangeAward(HttpServletRequest request) throws IOException {
        // 获取负责扫描二维码的店员信息
        WeChatAuth auth = getOperatorInfo(request);
        if (auth != null) {
            // 通过userId获取店员信息
            User operator = userService.getUserById(auth.getUser().getUserId());
            // 设置用户的session
            request.getSession().setAttribute("user", operator);
            // 解析微信回传过来的自定义参数state
            String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            WeChatInfo wechatInfo;
            try {
                wechatInfo = mapper.readValue(qrCodeInfo, WeChatInfo.class);
            } catch (Exception e) {
                return WeChatEnum.INNER_ERROR.getStateInfo();
            }
            // 校验二维码是否已经过期
            if (!checkQrCodeInfo(wechatInfo)) {
                return WeChatEnum.EXPIRE.getStateInfo();
            }
            // 获取用户奖品映射主键
            Long userAwardId = wechatInfo.getUserAwardId();
            // 获取顾客Id
            Integer customerId = wechatInfo.getCustomerId();
            // 将顾客信息, 操作员信息以及奖品信息封装成userAwardMap
            UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
            if (userAwardMap != null) {
                try {
                    // 检查该员工是否具有扫码权限
                    if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
                        return WeChatEnum.UNAUTHORIZED.getStateInfo();
                    }
                    // 修改奖品的领取状态
                    UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                        return WeChatEnum.EXCHANGE_SUCCESS.getStateInfo();
                    }
                } catch (RuntimeException e) {
                    return WeChatEnum.INNER_ERROR.getStateInfo();
                }

            }
        }
        return WeChatEnum.EXCHANGE_FAIL.getStateInfo();
    }

    /**
     * 获取扫描二维码的店员信息
     *
     * @param request HttpServletRequest
     * @return WeChatAuth
     */
    private WeChatAuth getOperatorInfo(HttpServletRequest request) {
        String code = HttpServletRequestUtil.getString(request, "code");
        return getWeChatAuth(request, code, weChatAuthService);
    }


    /**
     * 封装用户奖品映射实体类, 以供扫码使用, 主要将其领取状态变为已领取
     *
     * @param customerId  顾客id
     * @param userAwardId 顾客奖品id
     * @return UserAwardMap
     */
    private UserAwardMap compactUserAwardMap4Exchange(Integer customerId, Long userAwardId, User operator) {
        UserAwardMap userAwardMap = null;
        // 空值判断
        if (customerId != null && userAwardId != null && operator != null) {
            // 获取原有userAwardMap信息
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUsedStatus(1);
            User customer = new User();
            customer.setUserId(customerId);
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
        }
        return userAwardMap;
    }

    /**
     * 检查员工是否具有授权权限
     *
     * @param userId       userId
     * @param userAwardMap userAwardMap
     */
    private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
        // 取出该店铺所有的授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
        // 逐条遍历, 看看扫描二维码的员工是否具有扫码权限
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId && shopAuthMap.getEnableStatus() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param wechatInfo wechatInfo
     * @return 根据二维码携带的createTime判断其是否超过了1分钟, 超过十分钟则认为过期
     */
    private boolean checkQrCodeInfo(WeChatInfo wechatInfo) {
        // 空值判断
        if (wechatInfo != null &&
                wechatInfo.getUserAwardId() != null &&
                wechatInfo.getCustomerId() != null &&
                wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            return (nowTime - wechatInfo.getCreateTime()) <= TIME_LIMIT;
        } else {
            return false;
        }
    }
}
