package cn.qingweico.controller.frontend;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qingweico.dto.UserAwardMapExecution;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserAwardMap;
import cn.qingweico.entity.bo.AwardVO;
import cn.qingweico.enums.UserAwardMapStateEnum;
import cn.qingweico.service.AwardService;
import cn.qingweico.service.UserAwardMapService;
import cn.qingweico.service.UserService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/frontend")
public class AwardController {

    UserAwardMapService userAwardMapService;
    private static HashMap<String, Object> map = new HashMap<>();

    AwardService awardService;

    UserService userService;

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
     * 微信回传给的响应添加用户奖品映射信息的url
     */
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        AwardController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        AwardController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        AwardController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.exchange.url}")
    public void setExchangeUrl(String exchangeUrl) {
        AwardController.exchangeUrl = exchangeUrl;
    }

    @Autowired
    public void setUserAwardMapService(UserAwardMapService userAwardMapService) {
        this.userAwardMapService = userAwardMapService;
    }

    @Autowired
    public void setAwardService(AwardService awardService) {
        this.awardService = awardService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据顾客奖品映射id获取单条顾客奖品的映射信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/getAwardByUserAwardId")
    private JsonResult getAwardById(HttpServletRequest request) {
        // 获取前端传递过来的userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        // 空值判断
        AwardVO awardBO = new AwardVO();
        if (userAwardId > -1) {
            // 根据Id获取顾客奖品的映射信息, 进而获取奖品Id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            // 根据奖品Id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            awardBO.setAward(award);
            awardBO.setUsedStatus(userAwardMap.getUsedStatus());
            awardBO.setUserAwardMap(userAwardMap);
            return JsonResult.ok(award);
        } else {
            return new JsonResult(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 获取顾客的兑换列表
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listUserAwardMapsByCustomer")
    private JsonResult listUserAwardMapsByCustomer(HttpServletRequest request) {
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        // 确保用户id为非空
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUser(user);
            int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
            if (shopId > -1) {
                // 若店铺id为非空, 则将其添加进查询条件, 即查询该用户在某个店铺的兑换信息
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userAwardMapCondition.setShop(shop);
            }
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 若奖品名为非空, 则将其添加进查询条件里进行模糊查询
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }
            // 根据传入的查询条件分页获取用户奖品映射信息
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            return new JsonResult(ue);
        } else {
            return new JsonResult(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 在线兑换礼品
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @PostMapping("/addUserAwardMap")
    private JsonResult addUserAwardMap(HttpServletRequest request) {
        // 从session中获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        // 从前端请求中获取奖品Id
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        // 封装成用户奖品映射对象
        UserAwardMap userAwardMap = compactUserAwardMap(user, awardId);
        // 空值判断
        if (userAwardMap != null) {
            try {
                // 添加兑换信息
                UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
                if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                    return new JsonResult(ResponseStatusEnum.EXCHANGE_SUCCESS);
                } else {
                    return JsonResult.errorMsg(se.getStateInfo());
                }
            } catch (RuntimeException e) {
                log.error(e.toString());
                return JsonResult.error();
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHOOSE_AWARD);
        }
    }


    /**
     * 生成奖品的领取二维码, 供操作员扫描, 证明已领取, 微信扫一扫就能链接到对应的URL里面
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/generateQrcodeForAward")
    private void generateQrcodeForProduct(HttpServletRequest request, HttpServletResponse response) {
        // 获取前端传递过来的用户奖品映射Id
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        // 根据Id获取顾客奖品映射实体类对象
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        // 从session中获取顾客的信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if (userAwardMap != null &&
                user != null &&
                user.getUserId() != null &&
                userAwardMap.getUser().getUserId().equals(user.getUserId())) {
            // 获取当前时间戳, 以保证二维码的时间有效性, 精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将顾客奖品映射id, 顾客Id和timestamp传入content, 赋值到state中,
            // 这样微信获取到这些信息后会回传到用户奖品映射信
            // 息的添加方法里, 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{userAwardId:" + userAwardId +
                    ",customerId:" + user.getUserId() +
                    ",createTime:" + timeStamp + "}";
            CodeUtil.generateQrCode(response, content, urlPrefix, exchangeUrl, urlMiddle, urlSuffix);
        }
    }

    /**
     * 封装用户奖品映射实体类
     *
     * @param user    用户
     * @param awardId 奖品id
     * @return 用户与奖品之间的映射对象
     */
    private UserAwardMap compactUserAwardMap(User user, Long awardId) {
        UserAwardMap userAwardMap = null;
        // 空值判断
        if (user != null && user.getUserId() != null && awardId != -1) {
            userAwardMap = new UserAwardMap();
            // 根据用户Id获取用户实体类对象
            User userInfo = userService.getUserById((user.getUserId()));
            // 根据奖品Id获取奖品实体类对象
            Award award = awardService.getAwardById(awardId);
            userAwardMap.setUser(userInfo);
            userAwardMap.setAward(award);
            Shop shop = new Shop();
            shop.setShopId(award.getShopId());
            userAwardMap.setShop(shop);
            // 设置积分
            userAwardMap.setPoint(award.getPoint());
            userAwardMap.setCreateTime(new Date());
            // 设置兑换状态为已领取
            userAwardMap.setUsedStatus(1);
        }
        return userAwardMap;
    }
}
