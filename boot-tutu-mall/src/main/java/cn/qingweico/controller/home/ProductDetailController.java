package cn.qingweico.controller.home;

import cn.qingweico.common.Result;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.User;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * -------------- 主页商品详情 --------------
 *
 * @author zqw
 * @date 2020/11/14
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/u/product")
public class ProductDetailController {
    @Resource
    ProductService productService;

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
    private static String productMapUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.productmap.url}")
    public void setAuthUrl(String productMapUrl) {
        ProductDetailController.productMapUrl = productMapUrl;
    }


    /**
     * 根据商品id获取商品信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/detail")
    public Result productDetailPageInfo(HttpServletRequest request) {
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Map<String, Object> map = new HashMap<>(2);
        Product product;
        if (productId != -1) {
            product = productService.getProductById(productId);
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                // 用户未登录则不显示二维码
                map.put("needQRCode", false);
            } else {
                map.put("needQRCode", true);
            }
            map.put("product", product);
            return Result.ok(map);
        }
        log.error("productId: {}", productId);
        return Result.error();
    }

    /**
     * 生成商品的消费凭证二维码, 证明已消费
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/generateQrCode")
    private void generateQrCodeForProduct(HttpServletRequest request, HttpServletResponse response) {
        // 获取前端传递过来的商品Id
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        // 从session里获取当前顾客的信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if (productId != -1 && user != null && user.getId() != null) {
            // 获取当前时间戳, 以保证二维码的时间有效性, 精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将商品id, 顾客Id和timestamp传入content, 赋值到state中
            String content = "{productId:" + productId +
                    ",userId:" + user.getId() +
                    ",createTime:" + timeStamp + "}";
            CodeUtil.generateQrCode(response, content, urlPrefix, productMapUrl, urlMiddle, urlSuffix);
        }
    }
}
