package cn.qingweico.service.impl;


import cn.qingweico.cache.JedisUtil;
import cn.qingweico.dao.AreaDao;
import cn.qingweico.dto.AreaExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;
import cn.qingweico.exception.AreaOperationException;
import cn.qingweico.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/09/15
 */

@Service
public class AreaServiceImpl implements AreaService {

    private AreaDao areaDao;

    private JedisUtil.Keys jedisKeys;

    private JedisUtil.Strings jedisStrings;

    private static final Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    public void setAreaDao(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Autowired
    public void setJedisKeys(JedisUtil.Keys jedisKeys) {
        this.jedisKeys = jedisKeys;
    }

    @Autowired
    public void setJedisStrings(JedisUtil.Strings jedisStrings) {
        this.jedisStrings = jedisStrings;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Area> getAreaList() {
        // 定义redis的key
        String key = AREA_LIST_KEY;
        // 定义接收对象
        List<Area> areaList;
        // 定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        // 判断key是否存在
        if (!jedisKeys.exists(key)) {
            // 若不存在，则从数据库里面取出相应数据
            areaList = areaDao.queryAllArea();
            logger.info("查询数据库啦!");
            // 将相关的实体类集合转换成string,存入redis里面对应的key中
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        } else {
            // 若存在, 则直接从redis里面取出相应数据
            String jsonString = jedisStrings.get(key);
            // 指定要将string转换成的集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                // 将相关key对应的value里的的string转换成对象的实体类集合
                areaList = mapper.readValue(jsonString, javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }

    @Override
    public AreaExecution addArea(Area area) {
        // 空值判断, 主要是判断areaName不为空
        if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
            // 设置默认值
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.insertArea(area);
                if (effectedNum > 0) {
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new AreaOperationException("添加区域信息失败:" + e);
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    public AreaExecution modifyArea(Area area) {
        // 空值判断, 主要是areaId不为空
        if (area.getAreaId() != null && area.getAreaId() > 0) {
            // 设置默认值
            area.setLastEditTime(new Date());
            try {
                // 更新区域信息
                int effectedNum = areaDao.updateArea(area);
                if (effectedNum > 0) {
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new AreaOperationException("更新区域信息失败:" + e);
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    public AreaExecution getAreaById(Integer areaId) {
        Area area = areaDao.queryAreaById(areaId);
        if (area == null) {
            return new AreaExecution(AreaStateEnum.EMPTY);
        } else {
            deleteRedis4Area();
            return new AreaExecution(AreaStateEnum.SUCCESS, area);
        }
    }

    @Override
    public AreaExecution deleteAreaById(Integer areaId) {
        int effectNum = areaDao.deleteAreaById(areaId);
        if (effectNum > 0) {
            deleteRedis4Area();
            return new AreaExecution(AreaStateEnum.SUCCESS);
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    /**
     * 移除跟实体类相关的redis key-value
     */
    private void deleteRedis4Area() {
        String key = AREA_LIST_KEY;
        // 若redis存在对应的key,则将key清除
        if (jedisKeys.exists(key)) {
            jedisKeys.del(key);
        }
    }

}
