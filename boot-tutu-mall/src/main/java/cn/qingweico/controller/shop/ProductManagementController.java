package cn.qingweico.controller.shop;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ProductExecution;
import cn.qingweico.entity.Product;
import cn.qingweico.entity.ProductCategory;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.ProductStateEnum;
import cn.qingweico.exception.ProductOperationException;
import cn.qingweico.service.ProductCategoryService;
import cn.qingweico.service.ProductService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ProductManagementController {
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
     * @return Map
     */
    @PostMapping("/addOrModifyProduct")
    public JsonResult addOrModifyProduct(HttpServletRequest request) {
        if (!HttpServletRequestUtil.checkStatusChange(request) && !CodeUtil.checkVerificationCode(request)) {
            return JsonResult.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        Product product;
        ImageHolder imageHolder = null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<ImageHolder> productImageList = new ArrayList<>();
        String productString = HttpServletRequestUtil.getString(request, "productString");
        // 商品图片相关操作
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
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
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        // 开始添加或者修改商品数据
        try {
            product = objectMapper.readValue(productString, Product.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        // 判断是否为编辑状态
        boolean isModify = HttpServletRequestUtil.getBoolean(request, "isModify");
        if (isModify || HttpServletRequestUtil.checkStatusChange(request)) {
            return modifyProduct(request, product, imageHolder, productImageList);
        } else {
            return addProduct(request, product, imageHolder, productImageList);
        }
    }

    /**
     * 通过商品id获取商品信息
     *
     * @param productId 商品id
     * @return JsonResult
     */
    @GetMapping("/getProductById")
    public JsonResult getProductById(@RequestParam Long productId) {
        if (productId > -1) {
            HashMap<String, Object> map = new HashMap<>(2);
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            map.put("product", product);
            map.put("productCategoryList", productCategoryList);
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 获取店铺下所有的商品
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/getProductListByShop")
    public JsonResult getProductListByShop(HttpServletRequest request) {
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName, true);
            ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("productList", productExecution.getProductList());
            map.put("count", productExecution.getCount());
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 添加商品
     *
     * @param request          HttpServletRequest
     * @param product          商品实体
     * @param imageHolder      ImageHolder
     * @param productImageList 商品详情图列表
     * @return JsonResult
     */
    public JsonResult addProduct(HttpServletRequest request,
                                 Product product,
                                 ImageHolder imageHolder,
                                 List<ImageHolder> productImageList) {
        if (product != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution productExecution = productService.addProduct(product, imageHolder, productImageList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok();
                } else {
                    String msg = productExecution.getStateInfo();
                    return JsonResult.errorMsg(msg);
                }
            } catch (ProductOperationException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 修改商品信息
     *
     * @param request          HttpServletRequest
     * @param product          商品实体
     * @param imageHolder      ImageHolder
     * @param productImageList 商品详情图列表
     * @return JsonResult
     */
    public JsonResult modifyProduct(HttpServletRequest request,
                                    Product product,
                                    ImageHolder imageHolder,
                                    List<ImageHolder> productImageList) {
        if (product != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                ProductExecution productExecution = productService.modifyProduct(product, imageHolder, productImageList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    Integer productStatus = productExecution.getProduct().getEnableStatus();
                    return JsonResult.ok(productStatus);
                } else {
                    String msg = productExecution.getStateInfo();
                    return JsonResult.errorMsg(msg);
                }
            } catch (ProductOperationException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
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

    public static Product compactProductCondition(int shopId,
                                                  long productCategoryId,
                                                  String productName,
                                                  boolean isBackstage) {
        Product productCondition = new Product();
        if (!isBackstage) {
            productCondition.setEnableStatus(1);
        }
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}
