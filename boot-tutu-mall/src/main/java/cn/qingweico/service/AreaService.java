package cn.qingweico.service;

import cn.qingweico.dto.AreaExecution;
import cn.qingweico.entity.Area;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/09/15
 */
public interface AreaService {
    /**
     * 区域信息缓存的key
     */
    String AREA_LIST_KEY = "areaList";

    /**
     * 获取所有的区域信息列表的业务
     *
     * @return 区域信息列表
     */
    List<Area> getAreaList();

    /**
     * 增加区域信息
     *
     * @param area 地区
     * @return AreaExecution
     */
    AreaExecution addArea(Area area);

    /**
     * 修改区域信息
     *
     * @param area 地区
     * @return AreaExecution
     */
    AreaExecution modifyArea(Area area);

    /**
     * 根据区域id获取区域信息
     *
     * @param areaId 区域id
     * @return AreaExecution
     */
    AreaExecution getAreaById(Integer areaId);

    /**
     * 根据区域id删除区域信息
     *
     * @param areaId 区域id
     * @return AreaExecution
     */
    AreaExecution deleteAreaById(Integer areaId);
}
