package cn.qingweico.service.impl;

import cn.qingweico.dao.AwardDao;
import cn.qingweico.dto.AwardExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.Award;
import cn.qingweico.enums.AwardStateEnum;
import cn.qingweico.exception.AwardOperationException;
import cn.qingweico.service.AwardService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
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
public class AwardServiceImpl implements AwardService {

    private AwardDao awardDao;

    @Autowired
    public void setAwardDao(AwardDao awardDao) {
        this.awardDao = awardDao;
    }

    @Override
    public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize) {
        // 页转行
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(pageIndex, pageSize);
        // 根据查询条件分页取出奖品列表信息
        List<Award> awardList = awardDao.queryAwardList(awardCondition, rowIndex, pageSize);
        // 根据相同的查询条件返回该查询条件下的奖品总数
        int count = awardDao.queryAwardCount(awardCondition);
        AwardExecution awardExecution = new AwardExecution();
        awardExecution.setAwardList(awardList);
        awardExecution.setCount(count);
        return awardExecution;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardId(awardId);
    }

    /**
     * 条件奖品信息
     *
     * @param award     Award
     * @param thumbnail ImageHolder
     * @return AwardExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        // 空值判断
        if (award != null && award.getShopId() != null) {
            if("".equals(award.getAwardName())) {
                return new AwardExecution(AwardStateEnum.EMPTY_AWARD_NAME);
            }
            // 给award赋上初始值
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            // award默认可用, 即出现在前端展示系统中
            award.setEnableStatus(1);
            if (thumbnail != null) {
                // 若传入的图片信息不为空则更新图片
                addThumbnail(award, thumbnail);
            }
            try {
                // 添加奖品信息
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum <= 0) {
                    throw new AwardOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new AwardOperationException("创建商品失败: " + e);
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        }
        else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    /**
     * 修改奖品信息
     *
     * @param award     奖品
     * @param thumbnail 奖品缩略图
     * @return AwardExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
        // 空值判断
        if (award != null && award.getAwardId() != null) {
            if("".equals(award.getAwardName())) {
                return new AwardExecution(AwardStateEnum.EMPTY_AWARD_NAME);
            }
            award.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 通过awardId取出对应的实体类信息
                Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
                // 如果传输过程中存在图片流, 则删除原有图片
                if (tempAward.getAwardImage() != null) {
                    ImageUtil.deleteFileOrDirectory(tempAward.getAwardImage());
                }
                // 存储图片流, 获取相对路径
                addThumbnail(award, thumbnail);
            }
            try {
                // 根据传入的实体类修改相应的信息
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum <= 0) {
                    throw new AwardOperationException("更新商品信息失败");
                }
                return new AwardExecution(AwardStateEnum.SUCCESS, award);
            } catch (Exception e) {
                throw new AwardOperationException("更新商品信息失败: " + e);
            }
        } else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    /**
     * 存储图片流至指定路径并返回相对路径
     *
     * @param award     Award
     * @param thumbnail ImageHolder
     */
    private void addThumbnail(Award award, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnails(thumbnail, dest);
        award.setAwardImage(thumbnailAddr);
    }

}
