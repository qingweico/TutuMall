package cn.qingweico.controller.shop;

import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.entity.User;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.exception.ShopOperationException;
import cn.qingweico.service.AreaService;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
 * @author 周庆伟
 * @date 2020/10/05
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopManagementController {

    @Resource
    ShopService shopService;
    @Resource
    ShopCategoryService shopCategoryService;
    @Resource
    AreaService areaService;

    /**
     * 注册或修改店铺信息
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @PostMapping("/registerOrModifyShop")
    public JsonResult registerOrModifyShop(HttpServletRequest request) {
        if (!CodeUtil.checkVerificationCode(request)) {
            return JsonResult.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        String shopString = HttpServletRequestUtil.getString(request, "shopString");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop;
        try {
            shop = objectMapper.readValue(shopString, Shop.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        CommonsMultipartFile shopImage = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImage = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImage");
        }
        boolean isRegister = HttpServletRequestUtil.getBoolean(request, "isRegister");
        // 判断是否是注册操作
        if (isRegister) {
            return registerShop(request, shop, shopImage);
        } else {
            return modifyShop(request, shop, shopImage);
        }
    }

    /**
     * 获取店铺初始化信息
     *
     * @return JsonResult
     */
    @GetMapping("/getShopInitInfo")
    public JsonResult getShopInitInfo() {
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("shopCategoryList", shopCategoryList);
            map.put("areaList", areaList);
            return JsonResult.ok(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
    }


    /**
     * 根据店铺id获取店铺信息
     *
     * @param request request
     * @return JsonResult
     */
    @GetMapping("/getShopById")
    public JsonResult getShopById(HttpServletRequest request) {
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                HashMap<String, Object> map = new HashMap<>(2);
                map.put("shop", shop);
                map.put("areaList", areaList);
                return JsonResult.ok(map);
            } catch (Exception e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 获取店铺列表
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/getShopList")
    public JsonResult getShopList(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setUser(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 20);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("shopList", shopExecution.getShopList());
            map.put("user", user);
            return JsonResult.ok(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
    }

    @GetMapping("/getShopManagementInfo")
    public JsonResult getShopManagementInfo(HttpServletRequest request) {
        int shopId = HttpServletRequestUtil.getInteger(request, "shopId");
        HashMap<String, Object> map = new HashMap<>(2);
        if (shopId <= 0) {
            Object currentShop = request.getSession().getAttribute("currentShop");
            if (currentShop == null) {
                map.put("redirect", true);
                map.put("url", "/shop/list");
            } else {
                Shop shop = (Shop) currentShop;
                map.put("redirect", false);
                map.put("shopId", shop.getShopId());
            }
        } else {
            Shop shop = new Shop();
            shop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", shop);
            map.put("redirect", false);
            map.put("shopId", shopId);
        }
        return JsonResult.ok(map);
    }


    /**
     * 新增店铺
     * @param request HttpServletRequest
     * @param shop 店铺实体类
     * @param shopImage 店铺图片
     * @return JsonResult
     */
    public JsonResult registerShop(HttpServletRequest request, Shop shop, CommonsMultipartFile shopImage) {
        if (shop != null && shopImage != null) {
            User owner = (User) request.getSession().getAttribute("user");
            shop.setUser(owner);
            try {
                ImageHolder imageHolder = new ImageHolder(shopImage.getInputStream(), shopImage.getOriginalFilename());
                imageHolder.setImageForm("THUMBNAIL");
                ShopExecution shopExecution = shopService.addShop(shop, imageHolder);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null) {
                        shopList = new ArrayList<>(0);
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                    return JsonResult.errorCustom(ResponseStatusEnum.REGISTER_SHOP_SUCCESS);
                } else {
                    return JsonResult.errorMsg(shopExecution.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
          return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 修改店铺信息
     * @param request HttpServletRequest
     * @param shop 店铺实体类
     * @param shopImage 店铺图品
     * @return JsonResult
     */
    public JsonResult modifyShop(HttpServletRequest request, Shop shop, CommonsMultipartFile shopImage) {
        if (shop != null && shop.getShopId() != null) {
            User owner = (User) request.getSession().getAttribute("user");
            shop.setUser(owner);
            ImageHolder imageHolder = null;
            try {
                if (shopImage != null) {
                    imageHolder = new ImageHolder(shopImage.getInputStream(), shopImage.getOriginalFilename());
                    imageHolder.setImageForm("THUMBNAIL");
                }
                ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    return JsonResult.errorCustom(ResponseStatusEnum.UPDATE_SUCCESS);
                } else {
                    return JsonResult.errorMsg(shopExecution.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }
}
