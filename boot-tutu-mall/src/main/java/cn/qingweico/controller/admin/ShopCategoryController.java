package cn.qingweico.controller.admin;


import java.io.IOException;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.qingweico.common.Result;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopCategoryStateEnum;
import cn.qingweico.service.ShopCategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */

@RestController
@RequestMapping("/a/shopCategory")
public class ShopCategoryController {

    @Resource
    private ShopCategoryService shopCategoryService;

    /**
     * 获取所有店铺类别列表
     *
     * @return Result
     */

    @GetMapping("/list")
    private Result listShopCategories() {
        List<ShopCategory> list;
        list = shopCategoryService.getShopCategoryList(null);
        // 获取所有二级店铺类别列表, 并添加进以及店铺类别列表中
        list.addAll(shopCategoryService.getShopCategoryList(new ShopCategory()));
        return Result.ok(list);
    }

    /**
     * 获取所有一级店铺类别列表
     *
     * @return Result
     */
    @GetMapping("/1st-list")
    private Result firsLevelList() {
        List<ShopCategory> list;
        // 获取所有一级店铺类别列表
        list = shopCategoryService.getShopCategoryList(null);
        return Result.ok(list);
    }

    /**
     * 获取所有二级店铺类别列表
     *
     * @return Result
     */
    @GetMapping("/2se-list")
    private Result secondLevelList() {
        List<ShopCategory> list;
        // 获取所有二级店铺类别列表
        list = shopCategoryService.getShopCategoryList(new ShopCategory());
        return Result.ok(list);
    }

    /**
     * 添加或者修改店铺类别
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @PostMapping("/addOrModify")
    private Result addOrModifyShopCategory(@RequestBody ShopCategory shopCategory, HttpServletRequest request) throws IOException {

        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 取出缩略图并构建ImageHolder对象
            CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("ShopCategoryImage");
            if (thumbnailFile != null) {
                thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                thumbnail.setImageForm("THUMBNAIL");
            }

        }
        // 空值判断
        if (shopCategory != null && thumbnail != null) {
            Long id = shopCategory.getId();
            // 添加或者修改店铺类别信息
            ShopCategoryExecution ae;
            if (id != null) {
                ae = shopCategoryService.modifyShopCategory(shopCategory, thumbnail);
            } else {
                ae = shopCategoryService.addShopCategory(shopCategory, thumbnail);
            }
            if (ae.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        }
        return Result.error();
    }
}
