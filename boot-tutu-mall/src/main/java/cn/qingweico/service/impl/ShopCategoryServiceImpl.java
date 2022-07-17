package cn.qingweico.service.impl;

import cn.qingweico.dao.ShopCategoryDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopCategoryStateEnum;
import cn.qingweico.service.BaseService;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.JsonUtils;
import cn.qingweico.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/27
 */
@Slf4j
@Service
public class ShopCategoryServiceImpl extends BaseService implements ShopCategoryService {

    @Resource
    ShopCategoryDao shopCategoryDao;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory condition) {
        List<ShopCategory> shopCategoryList;
        String key = SHOP_CATEGORY_LIST_KEY;
        String jsonData;
        if (condition == null) {
            // 一级分类
            key = key + "_First_Level";
        } else if (condition.getParent() != null && condition.getParent().getId() != null) {
            // 该分类下所有的子类别
            key = key + "_Parent_" + condition.getParent().getId();
        } else {
            //所有子类别
            key = key + "_All";
        }
        if (!jedisKeys.exists(key)) {
            shopCategoryList = shopCategoryDao.queryShopCategory(condition);
            log.info("查询数据库");
            jsonData = JsonUtils.objectToJson(shopCategoryList);
            jedisStrings.set(key, jsonData);
        } else {
            jsonData = jedisStrings.get(key);
            shopCategoryList = JsonUtils.jsonToList(jsonData, ShopCategory.class);
        }
        return shopCategoryList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 设定默认值
        shopCategory.setCreateTime(new Date());
        shopCategory.setLastEditTime(new Date());
        if (thumbnail != null) {
            // 若上传有图片流，则进行存储操作, 并给shopCategory实体类设置上相对路径
            addThumbnail(shopCategory, thumbnail);
        }
        if (shopCategoryDao.insertShopCategory(shopCategory) > 0) {
            clearCache(SHOP_CATEGORY_LIST_KEY, true);
            return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS);
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 设定默认值
        shopCategory.setLastEditTime(new Date());
        if (thumbnail != null) {
            // 若上传的图片不为空, 则先获取之前的图片路径
            ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getId());
            if (tempShopCategory.getImgUrl() != null) {
                // 若之前图片不为空, 则先移除之前的图片
                ImageUtil.deleteFileOrDirectory(tempShopCategory.getImgUrl());
            }
            // 存储新的图片
            addThumbnail(shopCategory, thumbnail);
        }
        if (shopCategoryDao.updateShopCategory(shopCategory) > 0) {
            clearCache(SHOP_CATEGORY_LIST_KEY, true);
            return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS);
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.FAIL);
        }
    }


    /**
     * 存储图片
     *
     * @param shopCategory 店铺类别
     * @param thumbnail    缩略图
     */
    private void addThumbnail(ShopCategory shopCategory, ImageHolder thumbnail) {
        String dest = PathUtil.getShopCategoryPath();
        String thumbnailAddress = ImageUtil.generateThumbnails(thumbnail, dest);
        thumbnail.setImageForm("THUMBNAIL");
        shopCategory.setImgUrl(thumbnailAddress);
    }


    @Override
    public ShopCategory getShopCategoryById(Long shopCategoryId) {
        return shopCategoryDao.queryShopCategoryById(shopCategoryId);
    }
}
