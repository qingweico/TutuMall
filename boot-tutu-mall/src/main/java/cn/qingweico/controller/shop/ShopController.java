package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.entity.User;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.service.AreaService;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.service.ShopService;
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
 * -------------- 店铺 --------------
 *
 * @author zqw
 * @date 2020/10/05
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {

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
     * @return Result
     */
    @PostMapping("/registerOrModify")
    public Result registerOrModify(HttpServletRequest request, @RequestBody Shop shop) throws IOException {
        if (!CodeUtil.checkVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        CommonsMultipartFile shopImage = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImage = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImage");
        }
        if (shop == null) {
            return Result.error();
        }
        if (shopImage == null) {
            return Result.errorCustom(ResponseStatusEnum.UPLOAD_PIC_ERROR);
        }
        Long id = shop.getId();
        // 判断是否是注册操作
        if (id == null) {
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
    public Result getShopInitInfo() {
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
        areaList = areaService.getAreaList();
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("shopCategoryList", shopCategoryList);
        map.put("areaList", areaList);
        return Result.ok(map);
    }


    /**
     * 根据店铺id获取店铺信息
     *
     * @param request request
     * @return Result
     */
    @GetMapping("/get")
    public Result getShopById(HttpServletRequest request) {
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            Shop shop = shopService.getShopById(shopId);
            List<Area> areaList = areaService.getAreaList();
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("shop", shop);
            map.put("areaList", areaList);
            return Result.ok(map);
        }
        return Result.error();
    }

    /**
     * 获取店铺列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    public Result getShopList(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Shop shopCondition = new Shop();
        shopCondition.setCreator(user);
        ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 10);
        HashMap<String, Object> map = new HashMap<>(2);
        if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
            map.put("shopList", shopExecution.getShopList());
            map.put("user", user);
            return Result.ok(map);
        }
        return Result.errorMsg(shopExecution.getStateInfo());
    }

    @GetMapping("/getShopManagementInfo")
    public Result getShopManagementInfo(HttpServletRequest request) {
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        HashMap<String, Object> map = new HashMap<>(2);
        if (shopId <= 0) {
            Object currentShop = request.getSession().getAttribute("currentShop");
            if (currentShop == null) {
                map.put("redirect", true);
                map.put("url", "/shop/list");
            } else {
                Shop shop = (Shop) currentShop;
                map.put("redirect", false);
                map.put("shopId", shop.getId());
            }
        } else {
            Shop shop = new Shop();
            shop.setId(shopId);
            request.getSession().setAttribute("currentShop", shop);
            map.put("redirect", false);
            map.put("shopId", shopId);
        }
        return Result.ok(map);
    }


    /**
     * 新增店铺
     *
     * @param request   HttpServletRequest
     * @param shop      店铺实体类
     * @param shopImage 店铺图片
     * @return Result
     */
    public Result registerShop(HttpServletRequest request, Shop shop, CommonsMultipartFile shopImage) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        shop.setCreator(user);
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
            return Result.errorCustom(ResponseStatusEnum.REGISTER_SHOP_SUCCESS);
        } else {
            return Result.errorMsg(shopExecution.getStateInfo());
        }
    }

    /**
     * 修改店铺信息
     *
     * @param request   HttpServletRequest
     * @param shop      店铺实体类
     * @param shopImage 店铺图品
     * @return Result
     */
    public Result modifyShop(HttpServletRequest request, Shop shop, CommonsMultipartFile shopImage) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        shop.setCreator(user);
        ImageHolder imageHolder = null;
        if (shopImage != null) {
            imageHolder = new ImageHolder(shopImage.getInputStream(), shopImage.getOriginalFilename());
            imageHolder.setImageForm("THUMBNAIL");
        }
        ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
        if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
            return Result.errorCustom(ResponseStatusEnum.UPDATE_SUCCESS);
        } else {
            return Result.errorMsg(shopExecution.getStateInfo());
        }
    }
}
