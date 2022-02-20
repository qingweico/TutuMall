package cn.qingweico.service;

import cn.qingweico.dto.AwardExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.Award;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */
public interface AwardService {

    /**
     * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
     *
     * @param awardCondition 查询条件
     * @param pageIndex      起始索引
     * @param pageSize       每页的数量
     * @return AwardExecution
     */
    AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);

    /**
     * 根据awardId查询奖品信息
     *
     * @param awardId 奖品id
     * @return 奖品信息
     */
    Award getAwardById(long awardId);

    /**
     * 添加奖品信息，并添加奖品图片
     *
     * @param award     Award
     * @param thumbnail ImageHolder
     * @return AwardExecution
     */
    AwardExecution addAward(Award award, ImageHolder thumbnail);

    /**
     * 根据传入的奖品实例修改对应的奖品信息， 若传入图片则替换掉原先的图片
     *
     * @param award     奖品
     * @param thumbnail 奖品缩略图
     * @return AwardExecution
     */
    AwardExecution modifyAward(Award award, ImageHolder thumbnail);

}
