package cn.qingweico.service.impl;

import cn.qingweico.cache.JedisUtil;
import cn.qingweico.dao.ShopCategoryDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopCategoryExecution;
import cn.qingweico.entity.ShopCategory;
import cn.qingweico.enums.ShopCategoryStateEnum;
import cn.qingweico.exception.ShopCategoryOperationException;
import cn.qingweico.service.ShopCategoryService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PathUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 周庆伟
 * @date 2020/09/27
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    ShopCategoryDao shopCategoryDao;

    JedisUtil.Keys jedisKeys;

    JedisUtil.Strings jedisStrings;

    private final Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Autowired
    public void setShopCategoryDao(ShopCategoryDao shopCategoryDao) {
        this.shopCategoryDao = shopCategoryDao;
    }

    @Autowired
    public void setJedisKeys(JedisUtil.Keys jedisKeys) {
        this.jedisKeys = jedisKeys;
    }

    @Autowired
    public void setJedisStrings(JedisUtil.Strings jedisStrings) {
        this.jedisStrings = jedisStrings;
    }

    @Autowired
    public void setShopCategory(ShopCategoryDao shopCategory) {
        this.shopCategoryDao = shopCategory;
    }

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SHOP_CATEGORY_LIST_KEY;
        List<ShopCategory> shopCategoryList;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData;
        if (shopCategoryCondition == null) {
            //一级分类
            key = key + "_First_Level";
        } else if (shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            //该分类下所有的子类别
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else {
            //所有子类别
            key = key + "all";
        }
        if (!jedisKeys.exists(key)) {
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            logger.info("查询数据库啦!");
            try {
                jsonData = objectMapper.writeValueAsString(shopCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonData);
        } else {
            jsonData = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = objectMapper.readValue(jsonData, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ShopCategoryOperationException(e.toString());
            }
        }
        return shopCategoryList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 空值判断
        if (shopCategory != null) {
            // 设定默认值
            shopCategory.setCreateTime(new Date());
            shopCategory.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 若上传有图片流，则进行存储操作, 并给shopCategory实体类设置上相对路径
                addThumbnail(shopCategory, thumbnail);
            }
            try {
                // 往数据库添加店铺类别信息
                int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
                if (effectedNum > 0) {
                    // 删除店铺类别之前在redis里存储的一切key,for简单实现
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
                } else {
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("添加店铺类别信息失败:" + e);
            }
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 空值判断, 主要判断shopCategoryId不为空
        if (shopCategory.getShopCategoryId() != null && shopCategory.getShopCategoryId() > 0) {
            // 设定默认值
            shopCategory.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 若上传的图片不为空, 则先获取之前的图片路径
                ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
                if (tempShopCategory.getShopCategoryImage() != null) {
                    // 若之前图片不为空, 则先移除之前的图片
                    ImageUtil.deleteFileOrDirectory(tempShopCategory.getShopCategoryImage());
                }
                // 存储新的图片
                addThumbnail(shopCategory, thumbnail);
            }
            try {
                // 更新数据库信息
                int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
                if (effectedNum > 0) {
                    // 删除店铺类别之前在redis里存储的一切key, for简单实现
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
                } else {
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("更新店铺类别信息失败:" + e);
            }
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
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
        String thumbnailAddr = ImageUtil.generateThumbnails(thumbnail, dest);
        thumbnail.setImageForm("THUMBNAIL");
        shopCategory.setShopCategoryImage(thumbnailAddr);
    }

    /**
     * 移除跟实体类相关的redis key-value
     */
    private void deleteRedis4ShopCategory() {
        // 获取跟店铺类别相关的redis key
        Set<String> keySet = jedisKeys.keys(SHOP_CATEGORY_LIST_KEY + "*");
        for (String key : keySet) {
            // 逐条删除
            jedisKeys.del(key);
        }
    }

    @Override
    public ShopCategory getShopCategoryById(Integer shopCategoryId) {
        return shopCategoryDao.queryShopCategoryById(shopCategoryId);
    }
}
