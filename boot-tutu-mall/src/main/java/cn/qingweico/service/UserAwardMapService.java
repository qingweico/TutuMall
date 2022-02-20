package cn.qingweico.service;

import cn.qingweico.dto.UserAwardMapExecution;
import cn.qingweico.entity.UserAwardMap;
import cn.qingweico.exception.UserAwardMapOperationException;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

public interface UserAwardMapService {

    /**
     * 根据传入的查询条件分页获取映射列表及总数
     *
     * @param userAwardCondition 查询条件
     * @param pageIndex          起始索引
     * @param pageSize           每页的数量
     * @return UserAwardMapExecution
     */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);


    /**
     * 根据传入的查询条件分页获取映射列表及总数
     *
     * @param userAwardCondition 查询条件
     * @param pageIndex          起始索引
     * @param pageSize           每页的数量
     * @return UserAwardMapExecution
     */
    UserAwardMapExecution listReceivedUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据传入的Id获取映射信息
     *
     * @param userAwardMapId userAwardMapId
     * @return UserAwardMap
     */
    UserAwardMap getUserAwardMapById(long userAwardMapId);

    /**
     * 领取奖品，添加映射信息
     *
     * @param userAwardMap UserAwardMap
     * @return UserAwardMapExecution
     * @throws UserAwardMapOperationException
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

    /**
     * 修改映射信息, 这里主要修改奖品领取状态
     *
     * @param userAwardMap UserAwardMap
     * @return UserAwardMapExecution
     * @throws UserAwardMapOperationException
     */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

}
