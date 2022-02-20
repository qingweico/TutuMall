package cn.qingweico.dao;

import cn.qingweico.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周庆伟
 * @date 2020/10/2
 */
@Repository
public interface AreaDao {
    /**
     * 查询所有的地区信息
     *
     * @return 地区信息列表
     */
    List<Area> queryAllArea();

    /**
     * 根据区域id获取区域的信息
     *
     * @param areaId 区域id
     * @return Area
     */
    Area queryAreaById(Integer areaId);

    /**
     * 插入区域信息
     *
     * @param area Area
     * @return effectNum
     */
    int insertArea(Area area);

    /**
     * 更新区域信息
     *
     * @param area Area
     * @return effectNum
     */
    int updateArea(Area area);

    /**
     * 删除区域信息
     *
     * @param areaId 区域id
     * @return effectNum
     */
    int deleteAreaById(Integer areaId);

    /**
     * 批量删除区域列表
     *
     * @param areaIdList 区域id集合
     * @return effectNum
     */
    int batchDeleteArea(List<Long> areaIdList);
}
