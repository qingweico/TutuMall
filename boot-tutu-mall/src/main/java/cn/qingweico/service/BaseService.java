package cn.qingweico.service;


import cn.qingweico.cache.JedisUtil;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author zqw
 * @date 2022/7/12
 */
public class BaseService {
    /**
     * 区域信息缓存的key
     */
    public String AREA_LIST_KEY = "areaList";
    /**
     * 店铺类别信息缓存
     */
    public String SHOP_CATEGORY_LIST_KEY = "shopCategoryList";
    /**
     * 头条信息缓存的key
     */
    public String HEADLINE_LIST_KEY = "headLineList";
    @Resource
    public JedisUtil.Keys jedisKeys;
    @Resource
    public JedisUtil.Strings jedisStrings;

    /**
     * 移除跟实体类相关的redis key-value
     */
    public void clearCache(String key, boolean isMulti) {
        if (isMulti) {
            Set<String> keySet = jedisKeys.keys(key + "*");
            for (String k : keySet) {
                // 逐条删除
                jedisKeys.del(k);
            }
        } else {
            if (jedisKeys.exists(key)) {
                jedisKeys.del(key);
            }
        }
    }

    public void clearCache(String key) {
        clearCache(key, false);
    }

    /**
     * 移除以{@code Prefix}为前缀的缓存
     *
     * @param keyPrefix keyPrefix
     */
    public void clearPrefixCache(String keyPrefix) {
        Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
        for (String key : keySet) {
            jedisKeys.del(key);
        }
    }
}
