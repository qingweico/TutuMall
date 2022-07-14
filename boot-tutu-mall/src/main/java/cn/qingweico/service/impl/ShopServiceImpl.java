package cn.qingweico.service.impl;

import cn.qingweico.dao.ShopAuthRecordDao;
import cn.qingweico.dao.ShopDao;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.dto.ShopExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.ShopAuthRecord;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.enums.ShopStateEnum;
import cn.qingweico.service.ShopService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/28
 */
@Slf4j
@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    ShopDao shopDao;
    @Resource
    ShopAuthRecordDao shopAuthRecordDao;

    /**
     * 创建店铺
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
        shop.setEnableStatus(GlobalStatusEnum.disable.getVal());
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        int effectNumber = shopDao.registerShop(shop);
        if (effectNumber <= 0) {
            return new ShopExecution(ShopStateEnum.ADD_FAIL);
        } else {
            if (imageHolder.getImage() != null) {
                addShopImage(shop, imageHolder);
                // 更新店铺图片的地址
                effectNumber = shopDao.updateShop(shop);
                if (effectNumber <= 0) {
                    return new ShopExecution(ShopStateEnum.ADD_FAIL);
                }
                // 插入一条默认授权记录
                ShopAuthRecord shopAuthRecord = new ShopAuthRecord();
                shopAuthRecord.setUserId(shop.getCreatorId());
                shopAuthRecord.setShopId(shop.getId());
                shopAuthRecord.setTitle("店家");
                // 店铺创建后店家权限不可修改
                shopAuthRecord.setTitleFlag(GlobalStatusEnum.disable.getVal());
                shopAuthRecord.setCreateTime(new Date());
                shopAuthRecord.setLastEditTime(new Date());
                shopAuthRecord.setEnableStatus(GlobalStatusEnum.available.getVal());
                shopAuthRecordDao.insertShopAuthRecord(shopAuthRecord);
            }
        }
        return new ShopExecution(ShopStateEnum.SUCCESS);
    }

    /**
     * 根据店铺id查询店铺信息
     *
     * @param shopId 店铺id
     * @return Shop
     */
    @Override
    public Shop getShopById(Long shopId) {
        return shopDao.queryShopById(shopId);
    }

    /**
     * 修改店铺信息
     *
     * @param shop        店铺
     * @param imageHolder imageHolder
     * @return ShopExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) {
        // 判断是否需要处理图片
        if (imageHolder != null &&
                imageHolder.getImage() != null
                && imageHolder.getImageName() != null
                && !StringUtils.EMPTY.equals(imageHolder.getImageName())) {
            Shop s = shopDao.queryShopById(shop.getId());
            if (StringUtils.isNotBlank(s.getImgUrl())) {
                // 删除原来的店铺图片
                ImageUtil.deleteFileOrDirectory(s.getImgUrl());
            }
            addShopImage(shop, imageHolder);
        }
        //更新店铺信息
        shop.setLastEditTime(new Date());
        if (shopDao.updateShop(shop) <= 0) {
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
        return new ShopExecution(ShopStateEnum.SUCCESS);
    }

    /**
     * 根据条件分页列出所有的店铺
     *
     * @param shopCondition 查询条件
     * @param page          开始查询的位置
     * @param pageSize      每页的数量
     * @return ShopExecution
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int page, int pageSize) {
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        List<Shop> list = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        ShopExecution shopExecution = new ShopExecution();
        if (list != null) {
            shopExecution.setStateInfo(ShopStateEnum.SUCCESS.getStateInfo());
            shopExecution.setShopList(list);
        } else {
            shopExecution.setStateInfo(ShopStateEnum.INNER_ERROR.getStateInfo());
        }
        return shopExecution;
    }

    /**
     * 更新店铺的状态
     *
     * @param shop Shop
     * @return ShopExecution
     */
    @Override
    public ShopExecution updateShopStatus(Shop shop) {
        if (shopDao.updateShopStatus(shop) <= 0) {
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        } else {
            return new ShopExecution(ShopStateEnum.SUCCESS);
        }
    }

    /**
     * 添加店铺的详情图片
     *
     * @param shop        Shop
     * @param imageHolder ImageHolder
     */
    private void addShopImage(Shop shop, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(shop.getId());
        String shopImageAddress = ImageUtil.generateThumbnails(imageHolder, dest);
        shop.setImgUrl(shopImageAddress);
    }
}
