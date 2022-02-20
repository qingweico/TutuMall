package cn.qingweico.controller;

import cn.qingweico.service.AreaService;
import cn.qingweico.service.CacheService;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @author 周庆伟
 * @date 2020/12/03
 */
@Controller
public class CacheController {

   private CacheService cacheService;

   @Autowired
   public void setCacheService(CacheService cacheService) {
      this.cacheService = cacheService;
   }

   /**
    * 清除区域信息相关的所有redis缓存
    *
    * @return 清除缓存成功或者失败
    */
   @GetMapping(value = "/clearcache4area")
   private String clearCache4Area() {
      cacheService.removeFromCache(AreaService.AREA_LIST_KEY);
      return "shopadmin/operationsuccess";
   }

   /**
    * 清除头条相关的所有redis缓存
    *
    * @return 清除缓存成功或者失败
    */
   @GetMapping(value = "/clearcache4headline")
   private String clearCache4Headline() {
      cacheService.removeFromCache(HeadLineService.HEADLINE_LIST_KEY);
      return "shopadmin/operationsuccess";
   }

   /**
    * 清除店铺类别相关的所有redis缓存
    *
    * @return 清除缓存成功或者失败
    */
   @GetMapping(value = "/clearcache4shopcategory")
   private String clearCache4ShopCategory() {
      cacheService.removeFromCache(ShopCategoryService.SHOP_CATEGORY_LIST_KEY);
      return "shopadmin/operationsuccess";
   }

}
