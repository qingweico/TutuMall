package cn.qingweico.controller.frontend;

import cn.qingweico.entity.Product;
import cn.qingweico.entity.User;
import cn.qingweico.enums.ProductStateEnum;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */
@RestControllerAdvice
@RequestMapping("/frontend")
public class ProductDetailController {
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

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 根据商品id获取商品信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/productDetailPageInfo")
    public JsonResult productDetailPageInfo(HttpServletRequest request) {
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Map<String, Object> map = new HashMap<>(10);
        Product product;
        if (productId != -1) {
            product = productService.getProductById(productId);
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                map.put("needQRCode", false);
            } else {
                map.put("needQRCode", true);
            }
            map.put("product", product);
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorMsg(ProductStateEnum.EMPTY.getStateInfo());
        }
    }

    /**
     * 生成商品的消费凭证二维码, 供操作员扫描, 证明已消费, 微信扫一扫就能链接到对应的URL里面
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/generateQrCodeForProduct")
    private void generateQrCodeForProduct(HttpServletRequest request, HttpServletResponse response) {
        // 获取前端传递过来的商品Id
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        // 从session里获取当前顾客的信息
        User user = (User) request.getSession().getAttribute("user");
        // 空值判断
        if (productId != -1 && user != null && user.getUserId() != null) {
            // 获取当前时间戳, 以保证二维码的时间有效性, 精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将商品id, 顾客Id和timestamp传入content, 赋值到state中, 这样微信获取到这些信息后会回传到用户
            // 商品映射信息的添加方法里, 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{productId:" + productId +
                    ",customerId:" + user.getUserId() +
                    ",createTime:" + timeStamp + "}";
            CodeUtil.generateQrCode(response, content, urlPrefix, productMapUrl, urlMiddle, urlSuffix);
        }
    }
}
