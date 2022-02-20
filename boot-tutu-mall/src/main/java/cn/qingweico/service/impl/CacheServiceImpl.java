package cn.qingweico.service.impl;

import cn.qingweico.cache.JedisUtil;
import cn.qingweico.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 周庆伟
 * @date 2021/5/3
 */
@Service
public class CacheServiceImpl implements CacheService {

   @Autowired
   private JedisUtil.Keys jedisKeys;

   @Override
   public void removeFromCache(String keyPrefix) {
      Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
      for (String key : keySet) {
         jedisKeys.del(key);
      }
   }

}
