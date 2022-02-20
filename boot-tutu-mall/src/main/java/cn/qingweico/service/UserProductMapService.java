package cn.qingweico.service;

import cn.qingweico.dto.UserProductMapExecution;
import cn.qingweico.entity.UserProductMap;
import cn.qingweico.exception.UserProductMapOperationException;

/**
 * @author 周庆伟
 * @date 2020/10/8
 */

public interface UserProductMapService {
    /**
     * 通过传入的查询条件分页列出用户消费信息列表
     *
     * @param userProductCondition 查询条件
     * @param pageIndex            开始查询的索引
     * @param pageSize             每页的数量
     * @return UserProductMapExecution
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize);

    /**
     * 添加用户购买商品的关系映射
     *
     * @param userProductMap 用户与所购买商品之间的关系映射
     * @return UserProductMapExecution
     * @throws UserProductMapOperationException UserProductMapOperationException
     */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;
}
