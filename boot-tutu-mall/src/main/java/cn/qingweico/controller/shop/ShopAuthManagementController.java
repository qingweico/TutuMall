package cn.qingweico.controller.shop;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qingweico.dto.ShopAuthMapExecution;
import cn.qingweico.dto.UserAccessToken;
import cn.qingweico.entity.*;
import cn.qingweico.enums.ShopAuthMapStateEnum;
import cn.qingweico.enums.WeChatEnum;
import cn.qingweico.service.ShopAuthMapService;
import cn.qingweico.service.UserService;
import cn.qingweico.service.WeChatAuthService;
import cn.qingweico.utils.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/10/4
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopAuthManagementController {

    @Resource
    private ShopAuthMapService shopAuthMapService;
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
    @GetMapping("/listShopAuthMapsByShop")
    private JsonResult listShopAuthMapsByShop(HttpServletRequest request) {
        // 取出分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从Session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            // 分页取出该店铺下面的授权信息列表
            ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("shopAuthMapList", se.getShopAuthMapList());
            map.put("count", se.getCount());
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 根据店铺已授权id查询已授权用户详细信息
     *
     * @param shopAuthId 店铺已授权id
     * @return JsonResult
     */
    @GetMapping("/getShopAuthMapById")
    private JsonResult getShopAuthMapById(@RequestParam Long shopAuthId) {
        // 非空判断
        if (shopAuthId != null && shopAuthId > -1) {
            // 根据前台传入的shopAuthId查找对应的授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            return JsonResult.ok(shopAuthMap);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 添加或者修改授权信息
     *
     * @param shopAuthMapString 授权信息json
     * @param request           HttpServletRequest
     * @return JsonResult
     */
    @PostMapping("/modifyShopAuthMap")
    private JsonResult modifyShopAuthMap(String shopAuthMapString, HttpServletRequest request) {
        // 若为编辑状态需要输入验证码
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码校验
        if (!statusChange && !CodeUtil.checkVerificationCode(request)) {
            return JsonResult.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap;
        try {
            // 将前端传入的字符串json转换成shopAuthMap实例
            shopAuthMap = mapper.readValue(shopAuthMapString, ShopAuthMap.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        // 空值判断
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                // 检查被操作的对方是否为店家本身, 店家本身不支持修改
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    return JsonResult.errorCustom(ResponseStatusEnum.UN_AUTH);
                }
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok(se.getShopAuthMap().getEnableStatus());
                } else {
                    return JsonResult.errorMsg(se.getStateInfo());
                }
            } catch (RuntimeException e) {
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 检查被操作的对象是否可修改
     *
     * @param shopAuthId 店铺授权对象id
     * @return true or false
     */
    private boolean checkPermission(Long shopAuthId) {
        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
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
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    /**
     * 生成带有URL的二维码, 微信扫一扫就能链接到对应的URL里面
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/generateQrCodeForShopAuth")
    private void generateQrCodeForShopAuth(HttpServletRequest request, HttpServletResponse response) {
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getShopId() != null) {
            long timeStamp = System.currentTimeMillis();
            String content = "{\"shopId\":" + shop.getShopId() +
                    ",\"createTime\":" + timeStamp + "}";
            CodeUtil.generateQrCode(response, content, urlPrefix, authUrl, urlMiddle, urlSuffix);
        }
    }


    /**
     * 根据微信回传回来的参数添加店铺的授权信息
     *
     * @param request HttpServletRequest
     * @return String
     * @throws IOException IOException
     */
    @GetMapping("/addShopAuthMap")
    private String addShopAuthMap(HttpServletRequest request) throws IOException {
        // 从request里面获取微信用户的信息
        WeChatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            // 根据userId获取用户信息
            User user = userService.getUserById(auth.getUser().getUserId());
            // 将用户信息添加进user里
            request.getSession().setAttribute("user", user);
            // 解析微信回传过来的自定义参数state,由于之前进行了编码, 这里需要解码一下
            String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            WeChatInfo wechatInfo;
            try {
                // 将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa前缀, 转换成WeChatInfo实体类
                wechatInfo = mapper.readValue(qrCodeInfo, WeChatInfo.class);
            } catch (Exception e) {
                return WeChatEnum.INNER_ERROR.getStateInfo();
            }
            // 校验二维码是否已经过期
            if (!checkQrCodeInfo(wechatInfo)) {
                return WeChatEnum.EXPIRE.getStateInfo();
            }

            // 去重校验
            // 获取该店铺下所有的授权信息
            ShopAuthMapExecution allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 1, 999);
            List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
            for (ShopAuthMap sm : shopAuthList) {
                if (sm.getEmployee().getUserId().equals(user.getUserId())) {
                    return WeChatEnum.BE_AUTHORIZED.getStateInfo();
                }
            }

            try {
                // 根据获取到的内容, 添加微信授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return WeChatEnum.AUTHORIZATION_SUCCESS.getStateInfo();
                }
            } catch (RuntimeException e) {
                return WeChatEnum.INNER_ERROR.getStateInfo();
            }
        }
        return WeChatEnum.AUTHORIZATION_FAIL.getStateInfo();
    }


    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request HttpServletRequest
     * @return WeChatAuth
     */
    private WeChatAuth getEmployeeInfo(HttpServletRequest request) {
        String code = HttpServletRequestUtil.getString(request, "code");
        return getWeChatAuth(request, code, wechatAuthService);
    }

    static WeChatAuth getWeChatAuth(HttpServletRequest request, String code, WeChatAuthService wechatAuthService) {
        WeChatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            token = WeChatUtil.getUserAccessToken(code);
            if (token == null) {
                log.error("token为空!");
                throw new RuntimeException(ShopAuthMapStateEnum.NULL_TOKEN.getStateInfo());
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
            return false;
        }
    }
}
