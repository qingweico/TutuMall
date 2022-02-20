package cn.qingweico.controller.admin;


import java.net.URLDecoder;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.qingweico.dto.Constant4SuperAdmin;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopCategoryStateEnum;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */

@RestController
@RequestMapping("/superadmin")
public class ShopCategoryController {

    private ShopCategoryService shopCategoryService;

    private final Map<String, Object> map = new HashMap<>(5);

    @Autowired
    public void setShopCategoryService(ShopCategoryService shopCategoryService) {
        this.shopCategoryService = shopCategoryService;
    }

    /**
     * 获取所有店铺类别列表
     *
     * @return Map
     */

    @PostMapping("/listshopcategories")
    private Map<String, Object> listShopCategories() {
        List<ShopCategory> list;
        try {
            // 获取所有一级店铺类别列表
            list = shopCategoryService.getShopCategoryList(null);
            // 获取所有二级店铺类别列表, 并添加进以及店铺类别列表中
            list.addAll(shopCategoryService.getShopCategoryList(new ShopCategory()));
            map.put(Constant4SuperAdmin.PAGE_SIZE, list);
            map.put(Constant4SuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        return map;
    }

    /**
     * 获取所有一级店铺类别列表
     * @return model
     */
    @PostMapping("/list1stlevelshopcategories")
    private Map<String, Object> list1stLevelShopCategories() {
        List<ShopCategory> list;
        try {
            // 获取所有一级店铺类别列表
            list = shopCategoryService.getShopCategoryList(null);
            map.put(Constant4SuperAdmin.PAGE_SIZE, list);
            map.put(Constant4SuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        return map;
    }

    /**
     * 获取所有二级店铺类别列表
     * @return model
     */
    @PostMapping("/list2ndlevelshopcategories")
    private Map<String, Object> list2ndLevelShopCategorys() {
        List<ShopCategory> list;
        try {
            // 获取所有二级店铺类别列表
            list = shopCategoryService.getShopCategoryList(new ShopCategory());
            map.put(Constant4SuperAdmin.PAGE_SIZE, list);
            map.put(Constant4SuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        return map;
    }

    /**
     * 添加或者修改店铺类别
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @PostMapping("/addormodifyshopcategory")
    private Map<String, Object> addOrModifyShopCategory(HttpServletRequest request) {
        boolean isModify = HttpServletRequestUtil.getBoolean(request, "isModify");
        ObjectMapper mapper = new ObjectMapper();
        ShopCategory shopCategory;
        // 接收并转化相应的参数, 包括店铺类别信息以及图片信息
        String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryString");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            shopCategory = mapper.readValue(shopCategoryStr, ShopCategory.class);
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }

        try {
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("ShopCategoryImage");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                    thumbnail.setImageForm("THUMBNAIL");
                }

            }
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }
        // 空值判断
        if (shopCategory != null && thumbnail != null) {
            try {
                // decode可能有中文的地方
                shopCategory.setShopCategoryName((shopCategory.getShopCategoryName() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryName(), "UTF-8")));
                shopCategory.setShopCategoryDescription((shopCategory.getShopCategoryDescription() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryDescription(), "UTF-8")));
                // 添加或者修改店铺类别信息
                ShopCategoryExecution ae;
                if (isModify) {
                    ae = shopCategoryService.modifyShopCategory(shopCategory, thumbnail);
                } else {
                    ae = shopCategoryService.addShopCategory(shopCategory, thumbnail);
                }
                if (ae.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入店铺类别信息");
        }
        return map;
    }

}
