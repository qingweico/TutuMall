package cn.qingweico.service.impl;

import cn.qingweico.utils.JedisUtil;
import cn.qingweico.dao.HeadLineDao;
import cn.qingweico.dto.HeadLineExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.HeadLine;

import cn.qingweico.enums.HeadLineStateEnum;
import cn.qingweico.service.BaseService;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.JsonUtils;
import cn.qingweico.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zqw
 * @date 2020/09/17
 */
@Slf4j
@Service
public class HeadLineServiceImpl extends BaseService implements HeadLineService {

    @Resource
    HeadLineDao headLineDao;
    @Resource
    JedisUtil.Keys jedisKeys;
    @Resource
    JedisUtil.Strings jedisStrings;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        String key = HEADLINE_LIST_KEY;
        List<HeadLine> headLineList;
        String jsonData;
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        if (!jedisKeys.exists(key)) {
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            log.info("查询数据库");
            jsonData = JsonUtils.objectToJson(headLineList);
            jedisStrings.set(key, jsonData);
        } else {
            jsonData = jedisStrings.get(key);
            headLineList = JsonUtils.jsonToList(jsonData, HeadLine.class);
        }
        return headLineList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        // 设定默认值
        headLine.setCreateTime(new Date());
        headLine.setLastEditTime(new Date());
        // 若传入的头条图片为非空, 则存储图片并在实体类里将图片的相对路径设置上
        if (thumbnail != null) {
            addThumbnail(headLine, thumbnail);
        }
        if (headLineDao.insertHeadLine(headLine) > 0) {
            clearCache(HEADLINE_LIST_KEY);
            return new HeadLineExecution(HeadLineStateEnum.DELETE_SUCCESS);
        }
        return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        headLine.setLastEditTime(new Date());
        if (thumbnail != null) {
            // 若需要修改的图片流不为空, 则需要处理现有的图片
            HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine.getId());
            if (tempHeadLine.getImgUrl() != null) {
                // 若原来是存储有图片的, 则将原先图片删除
                ImageUtil.deleteFileOrDirectory(tempHeadLine.getImgUrl());
            }
            // 添加新的图片,并将新的图片相对路径设置到实体类里
            addThumbnail(headLine, thumbnail);
        }
        if (headLineDao.updateHeadLine(headLine) > 0) {
            clearCache(HEADLINE_LIST_KEY);
            return new HeadLineExecution(HeadLineStateEnum.DELETE_SUCCESS);
        }
        return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution deleteHeadlineById(long headLineId) {
        HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLineId);
        if (tempHeadLine.getImgUrl() != null) {
            ImageUtil.deleteFileOrDirectory(tempHeadLine.getImgUrl());
        }
        if (headLineDao.deleteHeadLine(headLineId) > 0) {
            clearCache(HEADLINE_LIST_KEY);
            return new HeadLineExecution(HeadLineStateEnum.DELETE_SUCCESS);
        }
        return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
        List<HeadLine> headLineList = headLineDao.queryHeadLineByIds(headLineIdList);
        for (HeadLine headLine : headLineList) {
            // 遍历头条列表, 若头条的图片非空, 则将图片删除
            if (headLine.getImgUrl() != null) {
                ImageUtil.deleteFileOrDirectory(headLine.getImgUrl());
            }
        }
        // 批量删除数据库中的头条信息
        if (headLineDao.batchDeleteHeadLine(headLineIdList) > 0) {
            clearCache(HEADLINE_LIST_KEY, true);
            return new HeadLineExecution(HeadLineStateEnum.DELETE_SUCCESS);
        }
        return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
    }

    /**
     * 设置头条缩略图
     *
     * @param headLine  头条
     * @param thumbnail 缩略图
     */
    private void addThumbnail(HeadLine headLine, ImageHolder thumbnail) {
        String dest = PathUtil.getHeadLineImagePath();
        thumbnail.setImageForm("NORMAL");
        String thumbnailAddress = ImageUtil.generateThumbnails(thumbnail, dest);
        headLine.setImgUrl(thumbnailAddress);
    }
}
