package cn.qingweico.service.impl;

import cn.qingweico.dao.UserProductMapDao;
import cn.qingweico.dao.UserShopMapDao;
import cn.qingweico.dto.UserProductMapExecution;
import cn.qingweico.entity.Shop;
import cn.qingweico.entity.User;
import cn.qingweico.entity.UserProductMap;
import cn.qingweico.entity.UserShopMap;
import cn.qingweico.enums.UserProductMapStateEnum;
import cn.qingweico.exception.UserProductMapOperationException;
import cn.qingweico.service.UserProductMapService;


import java.util.Date;
import java.util.List;

import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 周庆伟
 * @date 2020/10/9
 */

@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    private UserProductMapDao userProductMapDao;

    private UserShopMapDao userShopMapDao;

    @Autowired
    public void setUserProductMapDao(UserProductMapDao userProductMapDao) {
        this.userProductMapDao = userProductMapDao;
    }

    @Autowired
    public void setUserShopMapDao(UserShopMapDao userShopMapDao) {
        this.userShopMapDao = userShopMapDao;
    }

    /**
     * 列出用户所购买的所有商品
     * @param userProductCondition 查询条件
     * @param pageIndex            开始查询的索引
     * @param pageSize             每页的数量
     * @return UserProductMapExecution
     */
    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize) {
        // 空值判断
        if (userProductCondition != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
            // 依据查询条件分页取出列表
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition, beginIndex, pageSize);
            // 按照同等的查询条件获取总数
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
            userProductMapExecution.setUserProductMapList(userProductMapList);
            userProductMapExecution.setCount(count);
            return userProductMapExecution;
        } else {
            return null;
        }

    }

    /**
     * 添加消费记录
     * @param userProductMap 用户与所购买商品之间的关系映射
     * @return UserProductMapExecution
     * @throws UserProductMapOperationException UserProductMapOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap)
            throws UserProductMapOperationException {
        // 空值判断，主要确保顾客Id，店铺Id以及操作员Id非空
        if (userProductMap != null && userProductMap.getUser().getUserId() != null
                && userProductMap.getShop().getShopId() != null && userProductMap.getOperator().getUserId() != null) {
            // 设定默认值
            userProductMap.setCreateTime(new Date());
            try {
                // 添加消费记录
                int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
                if (effectedNum <= 0) {
                    throw new UserProductMapOperationException("添加消费记录失败");
                }
                // 若本次消费能够积分
                if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                    // 查询该顾客是否在店铺消费过
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),
                            userProductMap.getShop().getShopId());
                    if (userShopMap != null && userShopMap.getUserShopId() != null) {
                        // 若之前消费过，即有过积分记录，则进行总积分的更新操作
                        userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
                        effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (effectedNum <= 0) {
                            throw new UserProductMapOperationException("更新积分信息失败");
                        }
                    } else {
                        // 在店铺没有过消费记录，添加一条店铺积分信息(就跟初始化会员一样)
                        userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
                                userProductMap.getShop().getShopId(), userProductMap.getPoint());
                        effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectedNum <= 0) {
                            throw new UserProductMapOperationException("积分信息创建失败");
                        }
                    }
                }
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
            } catch (Exception e) {
                throw new UserProductMapOperationException("添加授权失败:" + e);
            }
        } else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USER_PRODUCT_INFO);
        }
    }

    /**
     * 封装顾客积分信息
     *
     * @param userId 顾客id
     * @param shopId 店铺id
     * @param point  积分
     * @return UserShopMap
     */
    private UserShopMap compactUserShopMap4Add(Integer userId, Integer shopId, Integer point) {
        UserShopMap userShopMap = null;
        // 空值判断
        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();
            User customer = new User();
            customer.setUserId(userId);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setUser(customer);
            userShopMap.setShop(shop);
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }

}
