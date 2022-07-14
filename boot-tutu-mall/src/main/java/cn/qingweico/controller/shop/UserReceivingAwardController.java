package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ShopAuthRecordExecution;
import cn.qingweico.dto.UserReceivingAwardRecordExecution;
import cn.qingweico.entity.*;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.UserReceivingAwardRecordStateEnum;
import cn.qingweico.enums.WeChatEnum;
import cn.qingweico.service.ShopAuthRecordService;
import cn.qingweico.service.UserReceivingAwardRecordService;
import cn.qingweico.service.UserService;
import cn.qingweico.service.WeChatAuthService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;

import static cn.qingweico.controller.shop.ShopAuthController.getWeChatAuth;


/**
 * -------------- 店铺奖品领取 --------------
 *
 * @author zqw
 * @date 2020/10/15
 */
@Slf4j
@RestController
@RequestMapping("/shop/u/award")
public class UserReceivingAwardController {

    @Resource
    UserReceivingAwardRecordService userAwardMapService;
    @Resource
    UserService userService;
    @Resource
    ShopAuthRecordService shopAuthMapService;
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
     * @return Result
     */
    @GetMapping("/listUserGotAward")
    private Result listUserGotAward(HttpServletRequest request) {
        // 从session里获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 空值判断
        if ((page > -1) && (pageSize > -1)
                && (currentShop != null)
                && (currentShop.getId() != null)) {
            UserReceivingAwardRecord userReceivingAwardRecord = new UserReceivingAwardRecord();
            userReceivingAwardRecord.setShopId(currentShop.getId());
            // 从请求中获取奖品名
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 如果需要按照奖品名称搜索, 则添加搜索条件
                Award award = new Award();
                award.setName(awardName);
                userReceivingAwardRecord.setAward(award);
            }
            // 分页返回结果
            UserReceivingAwardRecordExecution ue = userAwardMapService.listUserReceivedAwardRecord(userReceivingAwardRecord, page, pageSize);
            return Result.ok(ue.getUserReceivingAwardRecordList());
        }
        log.error("page: {}, pageSize: {} currentShop: {}", page, pageSize, currentShop);
        return Result.error();
    }

    /**
     * 扫顾客的奖品二维码并派发奖品,证明顾客已领取过
     *
     * @param request HttpServletRequest
     * @return String
     * @throws IOException IOException
     */
    @GetMapping("/acquire")
    private Result acquire(HttpServletRequest request) throws IOException {
        // 获取负责扫描二维码的店员信息
        WeChatAuthRecord auth = getOperatorInfo(request);
        if (auth != null) {
            // 通过userId获取店员信息
            User operator = userService.getUserById(auth.getUser().getId());
            // 设置用户的session
            request.getSession().setAttribute("user", operator);
            // 解析微信回传过来的自定义参数state
            String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
            WeChatInfo wechatInfo = JsonUtils.jsonToPojo(qrCodeInfo, WeChatInfo.class);
            // 校验二维码是否已经过期
            if (!checkQrCodeInfo(wechatInfo)) {
                return Result.ok(WeChatEnum.EXPIRE.getStateInfo());
            }
            if (wechatInfo == null) {
                log.error("wechatInfo is null");
                return Result.error();
            }
            Long userAwardId = wechatInfo.getUserAwardId();
            Long customerId = wechatInfo.getCustomerId();
            UserReceivingAwardRecord userReceivingAwardRecord = compactUserReceivedAwardCondition(customerId, userAwardId, operator);
            if (userReceivingAwardRecord != null) {
                // 检查该员工是否具有扫码权限
                if (!checkShopAuth(operator.getId(), userReceivingAwardRecord)) {
                    return Result.errorMsg(WeChatEnum.UNAUTHORIZED.getStateInfo());
                }
                // 修改奖品的领取状态
                UserReceivingAwardRecordExecution se = userAwardMapService.modifyUserReceivingAwardRecord(userReceivingAwardRecord);
                if (se.getState() == UserReceivingAwardRecordStateEnum.SUCCESS.getState()) {
                    return Result.ok(WeChatEnum.EXCHANGE_SUCCESS.getStateInfo());
                } else {
                    return Result.errorMsg(se.getStateInfo());
                }

            }
            log.error("userReceivingAwardRecord is null");
        }
        log.error("WeChatAuth: {}", auth);
        return Result.errorMsg(WeChatEnum.EXCHANGE_FAIL.getStateInfo());
    }

    /**
     * 获取扫描二维码的店员信息
     *
     * @param request HttpServletRequest
     * @return WeChatAuth
     */
    private WeChatAuthRecord getOperatorInfo(HttpServletRequest request) {
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
    private UserReceivingAwardRecord compactUserReceivedAwardCondition(Long customerId, Long userAwardId, User operator) {
        UserReceivingAwardRecord userReceivingAwardRecord = null;
        // 空值判断
        if (customerId != null && userAwardId != null && operator != null) {
            // 获取原有userAwardMap信息
            userReceivingAwardRecord = userAwardMapService.getUserReceivingAwardRecordById(userAwardId);
            userReceivingAwardRecord.setUsedStatus(GlobalStatusEnum.available.getVal());
            userReceivingAwardRecord.setUserId(customerId);
            userReceivingAwardRecord.setOperatorId(operator.getId());
        }
        return userReceivingAwardRecord;
    }

    /**
     * 检查员工是否具有授权权限
     *
     * @param userId                   userId
     * @param userReceivingAwardRecord userReceivingAwardRecord
     */
    private boolean checkShopAuth(long userId, UserReceivingAwardRecord userReceivingAwardRecord) {
        // 取出该店铺所有的授权信息
        ShopAuthRecordExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthRecordByShopId(userReceivingAwardRecord.getShopId(), 1, 10);
        // 逐条遍历, 看看扫描二维码的员工是否具有扫码权限
        for (ShopAuthRecord shopAuthRecord : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthRecord.getUserId() == userId && Objects.equals(shopAuthRecord.getEnableStatus(), GlobalStatusEnum.available.getVal())) {
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
            log.error("wechatInfo: {}", wechatInfo);
            return false;
        }
    }
}
