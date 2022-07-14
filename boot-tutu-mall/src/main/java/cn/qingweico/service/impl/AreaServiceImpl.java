package cn.qingweico.service.impl;


import cn.qingweico.cache.JedisUtil;
import cn.qingweico.dao.AreaDao;
import cn.qingweico.dto.AreaExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;
import cn.qingweico.service.AreaService;
import cn.qingweico.service.BaseService;
import cn.qingweico.utils.JsonUtils;
import cn.qingweico.utils.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/09/15
 */
@Slf4j
@Service
public class AreaServiceImpl extends BaseService implements AreaService {

    @Resource
    private AreaDao areaDao;
    @Resource
    private JedisUtil.Strings jedisStrings;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Area> getAreaList() {
        // 定义接收对象
        List<Area> areaList;
        // 判断key是否存在
        if (!jedisKeys.exists(AREA_LIST_KEY)) {
            // 若不存在,则从数据库里面取出相应数据
            areaList = areaDao.queryAllArea();
            log.info("查询数据库");
            // 将相关的实体类集合转换成string,存入redis里面对应的key中
            String jsonString = JsonUtils.objectToJson(areaList);
            jedisStrings.set(AREA_LIST_KEY, jsonString);
        } else {
            // 若存在, 则直接从redis里面取出相应数据
            String jsonString = jedisStrings.get(AREA_LIST_KEY);
            areaList = JsonUtils.jsonToList(jsonString, Area.class);
        }
        return areaList;
    }

    @Override
    public AreaExecution addArea(Area area) {
        long id = Snowflake.nextId();
        area.setId(id);
        area.setCreateTime(new Date());
        area.setLastEditTime(new Date());
        if (areaDao.insertArea(area) > 0) {
            clearCache(AREA_LIST_KEY);
            return new AreaExecution(AreaStateEnum.SUCCESS, area);
        } else {
            return new AreaExecution(AreaStateEnum.OP_ERROR);
        }
    }

    @Override
    public AreaExecution modifyArea(Area area) {
        area.setLastEditTime(new Date());
        if (areaDao.updateArea(area) > 0) {
            clearCache(AREA_LIST_KEY);
            return new AreaExecution(AreaStateEnum.SUCCESS, area);
        } else {
            return new AreaExecution(AreaStateEnum.OP_ERROR);
        }
    }

    @Override
    public AreaExecution getAreaById(Long areaId) {
        Area area = areaDao.queryAreaById(areaId);
        if (area == null) {
            return new AreaExecution(AreaStateEnum.EMPTY);
        } else {
            return new AreaExecution(AreaStateEnum.SUCCESS, area);
        }
    }

    @Override
    public AreaExecution deleteAreaById(Long areaId) {
        if (areaDao.deleteAreaById(areaId) > 0) {
            clearCache(AREA_LIST_KEY);
            return new AreaExecution(AreaStateEnum.SUCCESS);
        } else {
            return new AreaExecution(AreaStateEnum.OP_ERROR);
        }
    }
}
