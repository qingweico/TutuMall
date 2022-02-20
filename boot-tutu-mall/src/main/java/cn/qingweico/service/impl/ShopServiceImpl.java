package cn.qingweico.service.impl;

import cn.qingweico.dao.ShopAuthMapDao;
import cn.qingweico.dao.ShopDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopAuthMap;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.exception.ShopOperationException;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/28
 */
@Service
public class ShopServiceImpl implements ShopService {
    ShopDao shopDao;

    ShopAuthMapDao shopAuthMapDao;

    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Autowired
    public void setShopAuthMapDao(ShopAuthMapDao shopAuthMapDao) {
        this.shopAuthMapDao = shopAuthMapDao;
    }

    /**
     * 创建店铺
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectNumber = shopDao.registerShop(shop);
            if (effectNumber <= 0) {
                throw new ShopOperationException("店铺创建失败!");
            } else {
                if (imageHolder.getImage() != null) {
                    try {
                        // 存储图片
                        addShopImage(shop, imageHolder);
                    } catch (Exception e) {
                        throw new ShopOperationException("添加店铺图片失败" + e.getMessage());
                    }
                    // 更新店铺图片的地址
                    effectNumber = shopDao.updateShop(shop);
                    if (effectNumber <= 0) {
                        throw new ShopOperationException("店铺图片地址更新失败");
                    }
                    ShopAuthMap shopAuthMap = new ShopAuthMap();
                    shopAuthMap.setEmployee(shop.getUser());
                    shopAuthMap.setShop(shop);
                    shopAuthMap.setTitle("店家");
                    shopAuthMap.setTitleFlag(0);
                    shopAuthMap.setCreateTime(new Date());
                    shopAuthMap.setLastEditTime(new Date());
                    shopAuthMap.setEnableStatus(1);
                    try {
                        int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                        if (effectedNum <= 0) {
                            logger.error("addShop: 授权创建失败!");
                            throw new ShopOperationException("授权创建失败!");
                        }
                    } catch (Exception e) {
                        logger.error("insertShopAuthMap error: " + e.getMessage());
                        throw new ShopOperationException("授权创建失败!");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("注册店铺失败!" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /**
     * 根据店铺id查询店铺信息
     * @param shopId 店铺id
     * @return Shop
     */
    @Override
    public Shop getShopById(Integer shopId) {
        return shopDao.queryShopById(shopId);
    }

    /**
     * 修改店铺信息
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else if("".equals(shop.getShopName())) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP_NAME);
        }
        else {
            try {
                //判断是否需要处理图片
                if (imageHolder != null && imageHolder.getImage() != null && imageHolder.getImageName() != null && !"".equals(imageHolder.getImageName())) {
                    Shop tmpShop = shopDao.queryShopById(shop.getShopId());
                    if (tmpShop.getShopImage() != null) {
                        ImageUtil.deleteFileOrDirectory(tmpShop.getShopImage());
                    }
                    addShopImage(shop, imageHolder);
                }
                //更新店铺信息
                shop.setLastEditTime(new Date());
                int effectNumber = shopDao.updateShop(shop);
                if (effectNumber <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryShopById(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("修改店铺信息错误: " + e.getMessage());
            }
        }
    }

    /**
     * 根据条件分页列出所有的店铺
     * @param shopCondition 查询条件
     * @param pageIndex     开始查询的位置
     * @param pageSize      每页的数量
     * @return ShopExecution
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
        List<Shop> list = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (list != null) {
            shopExecution.setShopList(list);
            shopExecution.setCount(count);
        } else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    /**
     * 更新店铺的状态
     * @param shop Shop
     * @return ShopExecution
     */
    @Override
    public ShopExecution updateShopStatus(Shop shop) {
        if (shop.getEnableStatus() != null) {
            int effectNum = shopDao.updateShopStatus(shop);
            if (effectNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
        } else {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
    }

    /**
     * 添加店铺的详情图片
     * @param shop Shop
     * @param imageHolder ImageHolder
     */
    private void addShopImage(Shop shop, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImageAddress = ImageUtil.generateThumbnails(imageHolder, dest);
        shop.setShopImage(shopImageAddress);
    }
}
