package cn.qingweico.service.impl;

import cn.qingweico.cache.JedisUtil;
import cn.qingweico.dao.HeadLineDao;
import cn.qingweico.dto.HeadLineExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.HeadLine;

import cn.qingweico.enums.HeadLineStateEnum;
import cn.qingweico.exception.HeadLineOperationException;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.utils.ImageUtil;
import cn.qingweico.utils.PathUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 周庆伟
 * @date 2020/09/17
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {

    HeadLineDao headLineDao;

    JedisUtil.Keys jedisKeys;

    JedisUtil.Strings jedisStrings;

    @Autowired
    public void setJedisKeys(JedisUtil.Keys jedisKeys) {
        this.jedisKeys = jedisKeys;
    }

    @Autowired
    public void setJedisStrings(JedisUtil.Strings jedisStrings) {
        this.jedisStrings = jedisStrings;
    }

    private static final Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Autowired
    public void setHeadLineDao(HeadLineDao headLineDao) {
        this.headLineDao = headLineDao;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        String key = HEADLINE_LIST_KEY;
        List<HeadLine> headLineList;
        String jsonData;
        ObjectMapper objectMapper = new ObjectMapper();
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key = key + "-" + headLineCondition.getEnableStatus();
        }
        if (!jedisKeys.exists(key)) {
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            logger.info("查询数据库啦!");
            try {
                jsonData = objectMapper.writeValueAsString(headLineList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.toString());
            }
            jedisStrings.set(key, jsonData);
        } else {
            jsonData = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = objectMapper.readValue(jsonData, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new HeadLineOperationException(e.toString());
            }
        }
        return headLineList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        if (headLine != null) {
            // 设定默认值
            headLine.setCreateTime(new Date());
            headLine.setLastEditTime(new Date());
            // 若传入的头条图片为非空, 则存储图片并在实体类里将图片的相对路径设置上
            if (thumbnail != null) {
                addThumbnail(headLine, thumbnail);
            }
            try {
                // 往数据库里插入头条信息
                int effectedNum = headLineDao.insertHeadLine(headLine);
                if (effectedNum > 0) {
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new HeadLineOperationException("添加头条信息失败:" + e);
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        // 空值判断,主要是判断头条Id是否为空
        if (headLine.getLineId() != null && headLine.getLineId() > 0) {
            // 设定默认值
            headLine.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 若需要修改的图片流不为空, 则需要处理现有的图片
                HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine.getLineId());
                if (tempHeadLine.getLineImage() != null) {
                    // 若原来是存储有图片的, 则将原先图片删除
                    ImageUtil.deleteFileOrDirectory(tempHeadLine.getLineImage());
                }
                // 添加新的图片,并将新的图片相对路径设置到实体类里
                addThumbnail(headLine, thumbnail);
            }
            try {
                // 更新头条信息
                int effectedNum = headLineDao.updateHeadLine(headLine);
                if (effectedNum > 0) {
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new HeadLineOperationException("更新头条信息失败:" + e);
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution removeHeadLine(long headLineId) {
        // 空值判断, 主要判断头条Id是否为非空
        if (headLineId > 0) {
            try {
                // 根据Id查询头条信息
                HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLineId);
                if (tempHeadLine.getLineImage() != null) {
                    // 若头条原先存有图片, 则将该图片文件删除
                    ImageUtil.deleteFileOrDirectory(tempHeadLine.getLineImage());
                }
                // 删除数据库里对应的头条信息
                int effectedNum = headLineDao.deleteHeadLine(headLineId);
                if (effectedNum > 0) {
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new HeadLineOperationException("删除头条信息失败:" + e);
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
        // 空值判断
        if (headLineIdList != null && headLineIdList.size() > 0) {
            try {
                // 根据传入的id列表获取头条列表
                List<HeadLine> headLineList = headLineDao.queryHeadLineByIds(headLineIdList);
                for (HeadLine headLine : headLineList) {
                    // 遍历头条列表, 若头条的图片非空, 则将图片删除
                    if (headLine.getLineImage() != null) {
                        ImageUtil.deleteFileOrDirectory(headLine.getLineImage());
                    }
                }
                // 批量删除数据库中的头条信息
                int effectedNum = headLineDao.batchDeleteHeadLine(headLineIdList);
                if (effectedNum > 0) {
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new HeadLineOperationException("删除头条信息失败:" + e);
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
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
        headLine.setLineImage(thumbnailAddress);
    }

    /**
     * 移除跟实体类相关的redis key-value
     */
    private void deleteRedis4HeadLine() {
        // 获取跟头条相关的redis key
        Set<String> keySet = jedisKeys.keys(HEADLINE_LIST_KEY + "*");
        for (String key : keySet) {
            // 逐条删除
            jedisKeys.del(key);
        }
    }
}
