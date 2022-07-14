package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.*;
import cn.qingweico.entity.*;
import cn.qingweico.enums.UserConsumptionRecordStateEnum;
import cn.qingweico.enums.WeChatEnum;
import cn.qingweico.service.*;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.WeChatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * -------------- 店铺商品销售 --------------
 *
 * @author zqw
 * @date 2020/10/9
 */
@Slf4j
@RestController
@RequestMapping("/shop/product")
public class ShopProductSaleController {

    @Resource
    UserConsumptionRecordService userProductMapService;
    @Resource
    ProductSellDailyService productSellDailyService;
    @Resource
    WeChatAuthService weChatAuthService;
    @Resource
    ShopAuthRecordService shopAuthMapService;
    @Resource
    ProductService productService;

    private final static Integer TIME_LIMIT = 60000;

    /**
     * 店铺商品销售列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    private Result listUserProductMapsByShop(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验,主要确保shopId不为空
        if ((page > -1) && (pageSize > -1) &&
                (currentShop != null) &&
                (currentShop.getId() != null)) {
            // 添加查询条件
            UserConsumptionRecord userProductMapCondition = new UserConsumptionRecord();
            userProductMapCondition.setShopId(currentShop.getId());
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                // 若前端想按照商品名模糊查询,则传入productName
                Product product = new Product();
                product.setName(productName);
                userProductMapCondition.setProduct(product);
            }
            // 根据传入的查询条件获取该店铺的商品销售情况
            UserConsumptionRecordExecution ue = userProductMapService.userConsumptionRecordList(userProductMapCondition, page,
                    pageSize);
            return Result.ok(ue.getUserConsumptionRecordList());
        }
        return Result.error();
    }

    @GetMapping("/listProductSellDailyInfoByShop")
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>(10);
        // 获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验,主要确保shopId不为空
        if ((currentShop != null) && (currentShop.getId() != null)) {
            // 添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            // 获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            // 获取七天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            // 根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
            // 指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 商品名列表,保证唯一性
            LinkedHashSet<String> legendData = new LinkedHashSet<>();
            // x轴数据
            LinkedHashSet<String> xData = new LinkedHashSet<>();
            // 定义series
            List<EChartSeries> series = new ArrayList<>();
            // 日销量列表
            List<Integer> totalList = new ArrayList<>();
            // 当前商品名,默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                // 自动去重
                legendData.add(productSellDaily.getProduct().getName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getName())
                        && !currentProductName.isEmpty()) {
                    // 如果currentProductName不等于获取的商品名, 或者已遍历到列表的末尾, 且currentProductName不为空,
                    // 则是遍历到下一个商品的日销量信息了, 将前一轮遍历的信息放入series当中,
                    // 包括了商品名以及与商品对应的统计日期以及当日销量
                    EChartSeries es = new EChartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    // 重置totalList
                    totalList = new ArrayList<>();
                    // 变换下currentProductId为当前的productId
                    currentProductName = productSellDaily.getProduct().getName();
                    // 继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    // 如果还是当前的productId则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getName();
                }
                // 队列之末,需要将最后的一个商品销量信息也添加上
                if (i == productSellDailyList.size() - 1) {
                    EChartSeries es = new EChartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            map.put("series", series);
            map.put("legendData", legendData);
            // 拼接出xAxis
            List<EChartXAxis> xAxis = new ArrayList<>();
            EChartXAxis exa = new EChartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            map.put("xAxis", xAxis);
            map.put("success", true);
        } else {
            map.put("success", false);
            map.put("errorMessage", "empty shopId");
        }
        return map;
    }

    @GetMapping("/addUserProductMap")
    private String addUserProductMap(HttpServletRequest request) throws IOException {
        // 获取微信授权信息
        WeChatAuthRecord auth = getOperatorInfo(request);
        if (auth != null) {
            User operator = auth.getUser();
            request.getSession().setAttribute("user", operator);
            // 获取二维码里state携带的content信息并解码
            String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            WeChatInfo wechatInfo;
            try {
                // 将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa前缀, 转换成WechatInfo实体类
                wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WeChatInfo.class);
            } catch (Exception e) {
                return WeChatEnum.INNER_ERROR.getStateInfo();
            }
            // 校验二维码是否已经过期
            if (!checkQrCodeInfo(wechatInfo)) {
                return WeChatEnum.EXPIRE.getStateInfo();
            }
            // 获取添加消费记录所需要的参数并组建成userProductMap实例
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserConsumptionRecord userProductMap = compactUserConsumptionRecordCondition(customerId, productId, auth.getUser());
            // 空值校验
            if (userProductMap != null && customerId != -1) {
                try {
                    if (!checkShopAuth(operator.getId(), userProductMap)) {
                        return WeChatEnum.UNAUTHORIZED.getStateInfo();
                    }
                    // 添加消费记录
                    UserConsumptionRecordExecution se = userProductMapService.addUserConsumptionRecord(userProductMap);
                    if (se.getState() == UserConsumptionRecordStateEnum.SUCCESS.getState()) {
                        return WeChatEnum.PURCHASE_SUCCESS.getStateInfo();
                    }
                } catch (RuntimeException e) {
                    return WeChatEnum.INNER_ERROR.getStateInfo();
                }

            }
        }
        return WeChatEnum.PURCHASE_FAIL.getStateInfo();
    }

    /**
     * 根据code获取UserAccessToken, 进而通过token里的openId获取微信用户信息
     *
     * @param request HttpServletRequest
     * @return WeChatAuth
     */
    private WeChatAuthRecord getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WeChatAuthRecord auth = null;
        if (null != code) {
            UserAccessToken token;
            token = WeChatUtil.getUserAccessToken(code);
            if (token == null) {
                log.error("taken为空!");
                throw new RuntimeException("taken为空!");
            }
            String openId = token.getOpenId();
            request.getSession().setAttribute("openId", openId);
            auth = weChatAuthService.getWeChatAuthByOpenId(openId);
        }
        return auth;
    }

    /**
     * 根据传入的userId, productId以及操作员信息组建用户消费记录
     *
     * @param userId    顾客id
     * @param productId 商品id
     * @param operator  操作员id
     * @return 顾客与所消费商品之间的映射关系
     */
    private UserConsumptionRecord compactUserConsumptionRecordCondition(Long userId, Long productId, User operator) {
        UserConsumptionRecord userProductMap = null;
        if (userId != null && productId != null) {
            userProductMap = new UserConsumptionRecord();
            // 主要为了获取商品积分
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShopId(product.getShopId());
            userProductMap.setUserId(userId);
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
            userProductMap.setOperatorId(operator.getId());
        }
        return userProductMap;
    }

    /**
     * 检查扫码的人员是否有操作权限
     *
     * @param userId                用户id
     * @param userConsumptionRecord 顾客与所消费商品之间的映射关系
     * @return true or false
     */
    private boolean checkShopAuth(long userId, UserConsumptionRecord userConsumptionRecord) {
        // 获取该店铺的所有授权信息
        ShopAuthRecordExecution shopAuthRecordExecution = shopAuthMapService
                .listShopAuthRecordByShopId(userConsumptionRecord.getShopId(), 1, 10);
        for (ShopAuthRecord shopAuthRecord : shopAuthRecordExecution.getShopAuthMapList()) {
            // 看看是否给过该人员进行授权
            if (shopAuthRecord.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟, 超过十分钟则认为过期
     *
     * @param wechatInfo 二维码中包含的信息
     * @return 二维码是否失效
     */
    private boolean checkQrCodeInfo(WeChatInfo wechatInfo) {
        if (wechatInfo != null &&
                wechatInfo.getProductId() != null &&
                wechatInfo.getCustomerId() != null &&
                wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            return (nowTime - wechatInfo.getCreateTime()) <= TIME_LIMIT;
        } else {
            return false;
        }
    }
}
