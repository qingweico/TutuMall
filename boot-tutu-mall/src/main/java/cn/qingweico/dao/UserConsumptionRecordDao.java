package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.UserConsumptionRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/3
 */
@Repository
public interface UserConsumptionRecordDao {
    /**
     * 根据查询条件分页返回用户购买商品的记录列表(我的消费记录)
     *
     * @param userConsumptionRecord 查询条件
     * @param rowIndex              起始索引
     * @param pageSize              每页数量
     * @return List<UserProductMap>
     */
    List<UserConsumptionRecord> queryUserConsumptionRecordList(@Param("condition") UserConsumptionRecord userConsumptionRecord,
                                                               @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 添加一条用户消费记录
     *
     * @param userConsumptionRecord 用户消费记录
     * @return effectNum
     */
    int insertUserConsumptionRecord(UserConsumptionRecord userConsumptionRecord);
}
