package cn.qingweico.service.impl;

import cn.qingweico.dao.AwardDao;
import cn.qingweico.dto.AwardExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.Award;
import cn.qingweico.enums.AwardStateEnum;
import cn.qingweico.enums.GlobalStatusEnum;
import cn.qingweico.service.AwardService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PageCalculatorUtil;
import cn.qingweico.utils.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/10/15
 */

@Service
public class AwardServiceImpl implements AwardService {

    @Resource
    private AwardDao awardDao;

    @Override
    public AwardExecution getAwardList(Award awardCondition, int page, int pageSize) {
        // 页转行
        int rowIndex = PageCalculatorUtil.calculatorRowIndex(page, pageSize);
        // 根据查询条件分页取出奖品列表信息
        List<Award> awardList = awardDao.queryAwardList(awardCondition, rowIndex, pageSize);
        AwardExecution awardExecution = new AwardExecution();
        awardExecution.setAwardList(awardList);
        return awardExecution;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardById(awardId);
    }

    /**
     * 添加奖品
     *
     * @param award     Award
     * @param thumbnail ImageHolder
     * @return AwardExecution
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        award.setCreateTime(new Date());
        award.setLastEditTime(new Date());
        award.setEnableStatus(GlobalStatusEnum.available.getVal());
        if (thumbnail != null) {
            // 若传入的图片信息不为空则更新图片
            addThumbnail(award, thumbnail);
        }
        // 添加奖品信息
        if (awardDao.insertAward(award) <= 0) {
            return new AwardExecution(AwardStateEnum.INNER_ERROR);
        }
        return new AwardExecution(AwardStateEnum.ADD_SUCCESS);
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
        award.setLastEditTime(new Date());
        if (thumbnail != null) {
            // 通过awardId取出对应的实体类信息
            Award tempAward = awardDao.queryAwardById(award.getId());
            // 如果传输过程中存在图片流, 则删除原有图片
            if (tempAward.getImgUrl() != null) {
                ImageUtil.deleteFileOrDirectory(tempAward.getImgUrl());
            }
            // 存储图片流, 获取相对路径
            addThumbnail(award, thumbnail);
        }
        if (awardDao.updateAward(award) <= 0) {
            return new AwardExecution(AwardStateEnum.INNER_ERROR);
        }
        return new AwardExecution(AwardStateEnum.UPDATE_SUCCESS);
    }

    /**
     * 存储图片流至指定路径并返回相对路径
     *
     * @param award     Award
     * @param thumbnail ImageHolder
     */
    private void addThumbnail(Award award, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String thumbnailAddress = ImageUtil.generateThumbnails(thumbnail, dest);
        award.setImgUrl(thumbnailAddress);
    }

}
