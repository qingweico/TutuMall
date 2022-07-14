package cn.qingweico.controller.shop;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ShopAuthRecordExecution;
import cn.qingweico.dto.UserAccessToken;
import cn.qingweico.entity.*;
import cn.qingweico.enums.ShopAuthRecordStateEnum;
import cn.qingweico.enums.WeChatEnum;
import cn.qingweico.service.ShopAuthRecordService;
import cn.qingweico.service.UserService;
import cn.qingweico.service.WeChatAuthService;
import cn.qingweico.utils.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * -------------- 店家扫码授权管理 --------------
 *
 * @author zqw
 * @date 2020/10/4
 */
@Slf4j
@RestController
@RequestMapping("/shop/auth")
public class ShopAuthController {

    @Resource
    private ShopAuthRecordService shopAuthMapService;
    @Resource
    private WeChatAuthService wechatAuthService;
    @Resource
    private UserService userService;

    /**
     * 授权二维码的有效期
     */
    private final static Integer TIME_LIMIT = 60 * 1000;


    /**
     * 查询店铺下已授权用户列表
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/list")
    private Result listShopAuthMapsByShop(HttpServletRequest request) {
        // 取出分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从Session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((page > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getId() != null)) {
            // 分页取出该店铺下面的授权信息列表
            ShopAuthRecordExecution se = shopAuthMapService.listShopAuthRecordByShopId(currentShop.getId(), page, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("shopAuthMapList", se.getShopAuthMapList());
            map.put("count", se.getCount());
            return Result.ok(map);
        }
        log.error("page: {}, pageSize: {}, currentShop: {}", page, pageSize, currentShop);
        return Result.error();
    }

    /**
     * 根据店铺已授权id查询已授权用户详细信息
     *
     * @param shopAuthId 店铺已授权id
     * @return Result
     */
    @GetMapping("/get")
    private Result getShopAuthMapById(@RequestParam Long shopAuthId) {
        // 非空判断
        if (shopAuthId != null) {
            // 根据前台传入的shopAuthId查找对应的授权信息
            ShopAuthRecord shopAuthMap = shopAuthMapService.getShopAuthRecordById(shopAuthId);
            return Result.ok(shopAuthMap);
        }
        return Result.error();
    }

    /**
     * 添加或者修改授权信息
     *
     * @param shopAuthMap 店铺授权信息
     * @param request     HttpServletRequest
     * @return Result
     */
    @PostMapping("/modify")
    private Result modifyShopAuthMap(@RequestBody ShopAuthRecord shopAuthMap, HttpServletRequest request) {
        // 若为编辑状态需要输入验证码
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码校验
        if (!statusChange && !CodeUtil.checkVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        // 空值判断
        if (shopAuthMap != null && shopAuthMap.getId() != null) {
            // 检查被操作的对方是否为店家本身, 店家本身不支持修改
            if (!checkPermission(shopAuthMap.getId())) {
                return Result.errorCustom(ResponseStatusEnum.UN_AUTH);
            }
            ShopAuthRecordExecution se = shopAuthMapService.modifyShopAuthRecord(shopAuthMap);
            if (se.getState() == ShopAuthRecordStateEnum.SUCCESS.getState()) {
                return Result.ok(se.getShopAuthMap().getEnableStatus());
            } else {
                return Result.errorMsg(se.getStateInfo());
            }
        }
        log.error("shopAuthMap: {}", shopAuthMap);
        return Result.error();
    }

    /**
     * 检查被操作的对象是否可修改
     *
     * @param shopAuthId 店铺授权对象id
     * @return true or false
     */
    private boolean checkPermission(Long shopAuthId) {
        ShopAuthRecord grantedPerson = shopAuthMapService.getShopAuthRecordById(shopAuthId);
        return grantedPerson.getTitleFlag() != 0;
    }

    /**
     * 微信获取用户信息的api前缀
     */

    private static String urlPrefix;
    /**
     * 微信获取用户信息的api中间部分
     */
    private static String urlMiddle;
    /**
     * 微信获取用户信息的api后缀
     */
    private static String urlSuffix;
    /**
     * 微信回传给的响应添加授权信息的url
     */
    private static String authUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthController.authUrl = authUrl;
    }

    /**
     * 生成带有URL的二维码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/generateQrCodeForShopAuth")
    private void generateQrCodeForShopAuth(HttpServletRequest request, HttpServletResponse response) {
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getId() != null) {
            long timeStamp = System.currentTimeMillis();
            String content = "{\"shopId\":" + shop.getId() +
                    ",\"createTime\":" + timeStamp + "}";
            CodeUtil.generateQrCode(response, content, urlPrefix, authUrl, urlMiddle, urlSuffix);
        } else {
            log.error("shop: {}", shop);
        }
    }


    /**
     * 根据微信回传回来的参数添加店铺的授权信息
     *
     * @param request HttpServletRequest
     * @return String
     * @throws IOException IOException
     */
    @GetMapping("/add")
    private Result addShopAuthMap(HttpServletRequest request) throws IOException {
        // 从request里面获取微信用户的信息
        WeChatAuthRecord auth = getEmployeeInfo(request);
        if (auth != null && auth.getUser() != null) {
            // 根据userId获取用户信息
            User user = userService.getUserById(auth.getUser().getId());
            // 将用户信息添加进user里
            request.getSession().setAttribute("user", user);
            String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
            WeChatInfo wechatInfo = JsonUtils.jsonToPojo(qrCodeInfo, WeChatInfo.class);
            // 校验二维码是否已经过期
            if (!checkQrCodeInfo(wechatInfo)) {
                return Result.errorMsg(WeChatEnum.EXPIRE.getStateInfo());
            }
            if (wechatInfo == null) {
                log.error("WeChatInfo is null");
                return Result.error();
            }
            // 去重校验
            // 获取该店铺下所有的授权信息
            ShopAuthRecordExecution allMapList = shopAuthMapService.listShopAuthRecordByShopId(wechatInfo.getShopId(), 1, 10);
            List<ShopAuthRecord> shopAuthList = allMapList.getShopAuthMapList();
            for (ShopAuthRecord sm : shopAuthList) {
                if (sm.getId().equals(user.getId())) {
                    return Result.errorMsg(WeChatEnum.BE_AUTHORIZED.getStateInfo());
                }
            }
            ShopAuthRecord shopAuthRecord = new ShopAuthRecord();
            shopAuthRecord.setShopId(wechatInfo.getShopId());
            shopAuthRecord.setUserId(user.getId());
            shopAuthRecord.setTitle("员工");
            shopAuthRecord.setTitleFlag(1);
            ShopAuthRecordExecution se = shopAuthMapService.addShopAuthRecord(shopAuthRecord);
            if (se.getState() == ShopAuthRecordStateEnum.SUCCESS.getState()) {
                return Result.ok(WeChatEnum.AUTHORIZATION_SUCCESS.getStateInfo());
            }
        }
        return Result.errorMsg(WeChatEnum.AUTHORIZATION_FAIL.getStateInfo());
    }


    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request HttpServletRequest
     * @return WeChatAuth
     */
    private WeChatAuthRecord getEmployeeInfo(HttpServletRequest request) {
        String code = HttpServletRequestUtil.getString(request, "code");
        return getWeChatAuth(request, code, wechatAuthService);
    }

    static WeChatAuthRecord getWeChatAuth(HttpServletRequest request, String code, WeChatAuthService wechatAuthService) {
        WeChatAuthRecord auth = null;
        if (null != code) {
            UserAccessToken token;
            token = WeChatUtil.getUserAccessToken(code);
            if (token == null) {
                log.error("token为空!");
                throw new RuntimeException(ShopAuthRecordStateEnum.NULL_TOKEN.getStateInfo());
            }
            String openId = token.getOpenId();
            request.getSession().setAttribute("openId", openId);
            auth = wechatAuthService.getWeChatAuthByOpenId(openId);
        }
        return auth;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了1分钟, 超过一分钟则认为过期
     *
     * @param wechatInfo wechatInfo
     * @return 二维码是否过期
     */
    private boolean checkQrCodeInfo(WeChatInfo wechatInfo) {
        //空值判断
        if (wechatInfo != null &&
                wechatInfo.getShopId() != null &&
                wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            return (nowTime - wechatInfo.getCreateTime()) <= TIME_LIMIT;
        } else {
            log.error("wechatInfo: {}", wechatInfo);
            return false;
        }
    }
}
