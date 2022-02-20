package cn.qingweico.service.impl;


import cn.qingweico.dao.UserAwardMapDao;
import cn.qingweico.dao.UserShopMapDao;
import cn.qingweico.dto.UserAwardMapExecution;
import cn.qingweico.entity.UserAwardMap;
import cn.qingweico.entity.UserShopMap;
import cn.qingweico.enums.UserAwardMapStateEnum;
import cn.qingweico.exception.UserAwardMapOperationException;
import cn.qingweico.service.UserAwardMapService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {

    private UserAwardMapDao userAwardMapDao;

    private UserShopMapDao userShopMapDao;

    @Autowired
    public void setUserAwardMapDao(UserAwardMapDao userAwardMapDao) {
        this.userAwardMapDao = userAwardMapDao;
    }

    @Autowired
    public void setUserShopMapDao(UserShopMapDao userShopMapDao) {
        this.userShopMapDao = userShopMapDao;
    }

    /**
     * 列出用户已兑换的所有奖品
     * @param userAwardCondition 查询条件
     * @param pageIndex          起始索引
     * @param pageSize           每页的数量
     * @return UserAwardMapExecution
     */
    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
        // 空值判断
        if (userAwardCondition != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
            // 根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex, pageSize);
            // 返回总数
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
            UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
            userAwardMapExecution.setUserAwardMapList(userAwardMapList);
            userAwardMapExecution.setCount(count);
            return userAwardMapExecution;
        } else {
            return null;
        }
    }

    /**
     * 列出用户兑换奖品后已领取的奖品列表
     * @param userAwardCondition 查询条件
     * @param pageIndex          起始索引
     * @param pageSize           每页的数量
     * @return UserAwardMapExecution
     */
    @Override
    public UserAwardMapExecution listReceivedUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
        // 空值判断
        if (userAwardCondition != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
            // 根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryReceivedUserAwardMapList(userAwardCondition, beginIndex, pageSize);
            // 返回总数
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
            UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
            userAwardMapExecution.setUserAwardMapList(userAwardMapList);
            userAwardMapExecution.setCount(count);
            return userAwardMapExecution;
        } else {
            return null;
        }
    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }

    /**
     * 添加一条用户兑换奖品的记录
     * @param userAwardMap UserAwardMap
     * @return UserAwardMapExecution
     * @throws UserAwardMapOperationException UserAwardMapOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        // 空值判断, 主要是确定userId和shopId不为空
        if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
                && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
            // 设置默认值
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effectedNum;
                // 若该奖品需要消耗积分, 则将tb_user_shop_map对应的用户积分抵扣
                if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
                    // 根据用户Id和店铺Id获取该用户在店铺的积分
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
                            userAwardMap.getShop().getShopId());
                    // 判断该用户在店铺里是否有积分
                    if (userShopMap != null) {
                        // 若有积分, 必须确保店铺积分大于本次要兑换奖品需要的积分
                        if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
                            // 积分抵扣
                            userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                            // 更新积分信息
                            effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (effectedNum <= 0) {
                                throw new UserAwardMapOperationException("更新积分信息失败");
                            }
                        } else {
                            throw new UserAwardMapOperationException("积分不足无法领取");
                        }
                    } else {
                        // 在店铺没有积分, 则抛出异常
                        throw new UserAwardMapOperationException("在本店铺没有积分, 无法对换奖品");
                    }
                }
                // 插入礼品兑换信息
                effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    throw new UserAwardMapOperationException("领取奖励失败");
                }
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
            } catch (Exception e) {
                throw new UserAwardMapOperationException("领取奖励失败:" + e);
            }
        } else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USER_AWARD_INFO);
        }
    }

    /**
     * 修改奖品的领取状态
     * @param userAwardMap UserAwardMap
     * @return UserAwardMapExecution
     * @throws UserAwardMapOperationException UserAwardMapOperationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        // 空值判断, 主要是检查传入的userAwardId以及领取状态是否为空
        if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USER_AWARD_ID);
        } else {
            try {
                // 更新可用状态
                int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
                } else {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
                }
            } catch (Exception e) {
                throw new UserAwardMapOperationException("modifyUserAwardMap error: " + e.getMessage());
            }
        }
    }

}
