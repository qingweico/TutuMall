package cn.qingweico.dao;

import java.util.List;

import cn.qingweico.entity.UserReceivingAwardRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2020/10/2
 */
@Repository
public interface UserReceivingAwardRecordDao {
    /**
     * 根据传入进来的查询条件分页返回用户领取奖品记录
     *
     * @param userAwardCondition 查询条件
     * @param rowIndex           查询起始索引
     * @param pageSize           每页的数量
     * @return //
     */
    List<UserReceivingAwardRecord> queryUserReceivingAwardRecordList(@Param("userAwardCondition") UserReceivingAwardRecord userAwardCondition,
                                                                     @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    /**
     * 根据id返回一条用户领取奖品记录
     *
     * @param id 主键id
     * @return 用户领取奖品记录
     */
    UserReceivingAwardRecord queryUserReceivingAwardRecordById(long id);

    /**
     * 添加一条用户领取奖品记录
     *
     * @param userReceivingAwardRecord 用户领取奖品记录
     * @return effectNum
     */
    int insertUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord);

    /**
     * 更新用户领取奖品记录,主要更新奖品领取状态
     *
     * @param userReceivingAwardRecord 用户领取奖品记录
     * @return effectNum
     */
    int updateUserReceivingAwardRecord(UserReceivingAwardRecord userReceivingAwardRecord);
}
