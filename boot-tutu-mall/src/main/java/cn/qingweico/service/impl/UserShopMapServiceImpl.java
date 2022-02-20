package cn.qingweico.service.impl;

import cn.qingweico.dao.UserShopMapDao;
import cn.qingweico.dto.UserShopMapExecution;
import cn.qingweico.entity.UserShopMap;
import cn.qingweico.service.UserShopMapService;
import cn.qingweico.utils.PageCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/14
 */


@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    private UserShopMapDao userShopMapDao;

    @Autowired
    public void setUserShopMapDao(UserShopMapDao userShopMapDao) {
        this.userShopMapDao = userShopMapDao;
    }

    @Override
    public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
        // 空值判断
        if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
            // 页转行
            int beginIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
            // 根据传入的查询条件分页返回用户积分列表信息
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex, pageSize);
            // 返回总数
            int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
            UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
            userShopMapExecution.setUserShopMapList(userShopMapList);
            userShopMapExecution.setCount(count);
            return userShopMapExecution;
        } else {
            return null;
        }

    }

    @Override
    public UserShopMap getUserShopMap(long userId, long shopId) {
        return userShopMapDao.queryUserShopMap(userId, shopId);
    }
}
