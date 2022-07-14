package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ProductStateEnum;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * -------------- 店铺商品 --------------
 *
 * @author zqw
 * @date 2020/10/15
 */
@Slf4j
@RestController
@RequestMapping("/shop/product")
public class ShopProductController {
    @Resource
    ProductService productService;
    @Resource
    ProductCategoryService productCategoryService;
    /**
     * 商品详情图片最大数量
     */
    private static final int IMAGE_MAX_COUNT = 6;

    /**
     * 添加或者修改商品信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/addOrModify")
    public Result addOrModifyProduct(@RequestBody Product product, HttpServletRequest request) throws IOException {
        if (!HttpServletRequestUtil.checkStatusChange(request) && !CodeUtil.checkVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        if (product == null) {
            log.error("product: {}", (Object) null);
            return Result.error();
        }
        ImageHolder imageHolder = null;
        List<ImageHolder> productImageList = new ArrayList<>();
        // 商品图片相关操作
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile thumbnailFile = (CommonsMultipartFile)
                    multipartHttpServletRequest.getFile("thumbnail");
            //商品缩略图
            if (thumbnailFile != null) {
                imageHolder = new ImageHolder();
                imageHolder.setImage(thumbnailFile.getInputStream());
                imageHolder.setImageName(thumbnailFile.getOriginalFilename());
                // 设置商品图片的形式
                imageHolder.setImageForm("THUMBNAIL");
            }
            //商品详情图
            for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
                CommonsMultipartFile productImageFile = (CommonsMultipartFile)
                        multipartHttpServletRequest.getFile("productImage" + i);
                if (productImageFile != null) {
                    ImageHolder productImage = new ImageHolder(productImageFile.getInputStream(),
                            productImageFile.getOriginalFilename());
                    // 设置商品图片的形式
                    productImage.setImageForm("NORMAL");
                    productImageList.add(productImage);
                } else {
                    break;
                }
            }
        }
        Long id = product.getId();
        // 判断是否为编辑状态
        if (id != null || HttpServletRequestUtil.checkStatusChange(request)) {
            return modifyProduct(request, product, imageHolder, productImageList);
        } else {
            return addProduct(request, product, imageHolder, productImageList);
        }
    }

    /**
     * 通过商品id获取商品信息
     *
     * @param productId 商品id
     * @return Result
     */
    @GetMapping("/get")
    public Result getProductById(@RequestParam Long productId) {
        HashMap<String, Object> map = new HashMap<>(2);
        Product product = productService.getProductById(productId);
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getId());
        map.put("product", product);
        map.put("productCategoryList", productCategoryList);
        return Result.ok(map);
    }

    /**
     * 获取店铺下所有的商品
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/listInShop")
    public Result listInShop(HttpServletRequest request) {
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (page > -1 && pageSize > -1 && currentShop != null && currentShop.getId() != null) {
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getId(), productCategoryId, productName, true);
            ProductExecution productExecution = productService.getProductList(productCondition, page, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("productList", productExecution.getProductList());
            map.put("count", productExecution.getCount());
            return Result.ok(map);
        }
        log.error("page: {}, pageSize: {}, currentShop: {}", page, pageSize, currentShop);
        return Result.error();
    }

    /**
     * 添加商品
     *
     * @param request          HttpServletRequest
     * @param product          商品实体
     * @param imageHolder      ImageHolder
     * @param productImageList 商品详情图列表
     * @return Result
     */
    public Result addProduct(HttpServletRequest request,
                             Product product,
                             ImageHolder imageHolder,
                             List<ImageHolder> productImageList) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        product.setShop(currentShop);
        ProductExecution productExecution = productService.addProduct(product, imageHolder, productImageList);
        if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
            return Result.ok();
        } else {
            String msg = productExecution.getStateInfo();
            return Result.errorMsg(msg);
        }
    }

    /**
     * 修改商品信息
     *
     * @param request          HttpServletRequest
     * @param product          商品实体
     * @param imageHolder      ImageHolder
     * @param productImageList 商品详情图列表
     * @return Result
     */
    public Result modifyProduct(HttpServletRequest request,
                                Product product,
                                ImageHolder imageHolder,
                                List<ImageHolder> productImageList) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Shop shop = new Shop();
        shop.setId(currentShop.getId());
        product.setShop(shop);
        ProductExecution productExecution = productService.modifyProduct(product, imageHolder, productImageList);
        if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
            Integer productStatus = productExecution.getProduct().getEnableStatus();
            return Result.ok(productStatus);
        } else {
            String msg = productExecution.getStateInfo();
            return Result.errorMsg(msg);
        }
    }

    /**
     * 组合查询条件
     *
     * @param shopId            店铺id
     * @param productCategoryId 商品类别id
     * @param productName       商品名称
     * @return Product
     */

    public static Product compactProductCondition(long shopId,
                                                  long productCategoryId,
                                                  String productName,
                                                  boolean isBackstage) {
        Product productCondition = new Product();
        if (!isBackstage) {
            productCondition.setEnableStatus(1);
        }
        Shop shop = new Shop();
        shop.setId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setName(productName);
        }
        return productCondition;
    }
}
