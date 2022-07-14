package cn.qingweico.controller.frontend;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qingweico.common.Result;
import cn.qingweico.dto.UserReceivingAwardRecordExecution;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserReceivingAwardRecord;
import cn.qingweico.entity.bo.AwardVO;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.UserReceivingAwardRecordStateEnum;
import cn.qingweico.service.AwardService;
import cn.qingweico.service.UserReceivingAwardRecordService;
import cn.qingweico.service.UserService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.ResponseStatusEnum;
import jdk.nashorn.internal.objects.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * -------------- 主页奖品 --------------
 *
 * @author zqw
 * @date 2020/10/15
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u")
public class AwardController {
    @Resource
    AwardService awardService;
    @Resource
    UserReceivingAwardRecordService userReceivingAwardRecordService;
    @Resource
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

    /**
     * //
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/getAwardByUserAwardId")
    private Result getAwardById(HttpServletRequest request) {
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        // 空值判断
        AwardVO awardBO = new AwardVO();
        if (userAwardId > -1) {
            // 根据id获取顾客奖品的映射信息, 进而获取奖品Id
            UserReceivingAwardRecord userReceivingAwardRecord = userReceivingAwardRecordService.getUserReceivingAwardRecordById(userAwardId);
            // 根据奖品Id获取奖品信息
            Award award = awardService.getAwardById(userReceivingAwardRecord.getAward().getId());
            awardBO.setAward(award);
            awardBO.setUsedStatus(userReceivingAwardRecord.getUsedStatus());
            awardBO.setUserAwardMap(userReceivingAwardRecord);
            return Result.ok(award);
        }
        log.error("userAwardId: {}", userAwardId);
        return Result.error();
    }

    /**
     * 获取用户的奖品领取列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/getAcquiredAwardList")
    private Result getAcquiredAwardList(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session中获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        // 确保用户id为非空
        if ((page > -1) && (pageSize > -1) && (user != null) && (user.getId() != null)) {
            UserReceivingAwardRecord userReceivingAwardRecord = new UserReceivingAwardRecord();
            userReceivingAwardRecord.setUserId(user.getId());
            long shopId = HttpServletRequestUtil.getInteger(request, "shopId");
            if (shopId > -1) {
                // 若店铺id为非空, 则将其添加进查询条件, 即查询该用户在某个店铺的兑换信息
                userReceivingAwardRecord.setShopId(shopId);
            }
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 若奖品名为非空, 则将其添加进查询条件里进行模糊查询
                Award award = new Award();
                award.setName(awardName);
                userReceivingAwardRecord.setAward(award);
            }
            // 根据传入的查询条件分页获取用户奖品映射信息
            UserReceivingAwardRecordExecution ue = userReceivingAwardRecordService.listUserReceivedAwardRecord(userReceivingAwardRecord, page, pageSize);
            return Result.ok(ue.getUserReceivingAwardRecordList());
        }
        log.error("page: {}, pageSize: {}, user: {}", page, pageSize, user);
        return Result.error();
    }

    /**
     * 用户在线兑换礼品
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/addUserReceivingAwardRecord")
    private Result addUserReceivingAwardRecord(HttpServletRequest request) {

        // 获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        UserReceivingAwardRecord userAwardMap = compactUserReceivingAwardRecord(user, awardId);
        if (userAwardMap != null) {
            UserReceivingAwardRecordExecution se = userReceivingAwardRecordService.addUserReceivingAwardRecord(userAwardMap);
            if (se.getState() == UserReceivingAwardRecordStateEnum.SUCCESS.getState()) {
                return new Result(ResponseStatusEnum.EXCHANGE_SUCCESS);
            } else {
                return Result.errorMsg(se.getStateInfo());
            }
        }
        return Result.error();
    }


    /**
     * 生成奖品的领取二维码, 店铺进行扫描, 证明已领取
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/generateQrcodeForAward")
    private void generateQrcodeForProduct(HttpServletRequest request, HttpServletResponse response) {
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        // 根据Id获取顾客奖品映射实体类对象
        UserReceivingAwardRecord userReceivingAwardRecord = userReceivingAwardRecordService.getUserReceivingAwardRecordById(userAwardId);
        // 从session中获取顾客的信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断;保证当前登录的用户即为奖品兑换的用户
        if (userReceivingAwardRecord != null && user != null && user.getId() != null && userReceivingAwardRecord.getUserId().equals(user.getId())) {
            // 获取当前时间戳, 以保证二维码的时间有效性, 精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将顾客奖品映射id, 顾客id和timestamp传入content, 赋值到state中,
            // 这样微信获取到这些信息后会回传到用户奖品映射信息的添加方法里
            String content = "{userAwardId:" + userAwardId + ",userId:" + user.getId() + ",createTime:" + timeStamp + "}";
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
    private UserReceivingAwardRecord compactUserReceivingAwardRecord(User user, Long awardId) {
        UserReceivingAwardRecord userReceivingAwardRecord = null;
        // 空值判断
        if (user != null && user.getId() != null && awardId != -1) {
            userReceivingAwardRecord = new UserReceivingAwardRecord();
            // 根据用户Id获取用户实体类对象
            User userInfo = userService.getUserById((user.getId()));
            // 根据奖品Id获取奖品实体类对象
            Award award = awardService.getAwardById(awardId);
            userReceivingAwardRecord.setUserId(userInfo.getId());
            userReceivingAwardRecord.setAward(award);
            userReceivingAwardRecord.setShopId(award.getShopId());
            // 设置积分
            userReceivingAwardRecord.setPoint(award.getPoint());
            userReceivingAwardRecord.setCreateTime(new Date());
            // 设置兑换状态为已领取
            userReceivingAwardRecord.setUsedStatus(GlobalStatusEnum.available.getVal());
        }
        return userReceivingAwardRecord;
    }
}
