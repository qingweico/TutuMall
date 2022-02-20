package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/3
 */


@Repository
public interface UserProductMapDao {
    /**
     * 根据查询条件分页返回用户购买商品的记录列表
     *
     * @param userProductCondition 查询条件
     * @param rowIndex             起始索引
     * @param pageSize             每页数量
     * @return List<UserProductMap>
     */
    List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition,
                                                 @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合queryUserProductMapList根据相同的查询条件返回用户购买商品的记录总数
     *
     * @param userProductCondition 查询条件
     * @return 用户购买商品的记录总数
     */
    int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

    /**
     * 添加一条用户购买商品的记录
     *
     * @param userProductMap 用户购买商品的记录
     * @return effectNum
     */
    int insertUserProductMap(UserProductMap userProductMap);
}
