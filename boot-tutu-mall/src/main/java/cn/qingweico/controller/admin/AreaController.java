package cn.qingweico.controller.admin;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qingweico.dto.AreaExecution;
import cn.qingweico.dto.Constant4SuperAdmin;
import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;
import cn.qingweico.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */
@RestControllerAdvice
@RequestMapping("/superadmin")
public class AreaController {

    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);

    private AreaService areaService;

    private final Map<String, Object> map = new HashMap<>(5);

    @Autowired
    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }


    /**
     * 获取所有的区域信息
     *
     * @return Map
     */
    @GetMapping("/listarea")
    private Map<String, Object> listArea() {
        long startTime = System.currentTimeMillis();
        List<Area> list;
        try {
            // 获取区域列表
            list = areaService.getAreaList();
            map.put(Constant4SuperAdmin.PAGE_SIZE, list);
            map.put(Constant4SuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - startTime);
        return map;
    }

    /**
     * 添加或者修改区域信息
     *
     * @param params Map
     * @return Map
     */
    @PostMapping("/addorupdatearea")
    private Map<String, Object> addOrUpdateArea(@RequestBody Map<String, Object> params) {
        boolean isUpdate = (boolean) params.get("isUpdate");
        ObjectMapper mapper = new ObjectMapper();
        String areaString = (String) params.get("areaString");
        Area area;
        try {
            // 接收前端传递过来的area json字符串信息并转换成Area实体类实例
            area = mapper.readValue(areaString, Area.class);
            // decode可能有中文的地方
            area.setAreaName((area.getAreaName() == null) ? null : URLDecoder.decode(area.getAreaName(), "UTF-8"));
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }
        // 空值判断
        if (area.getAreaName() != null) {
            try {
                AreaExecution ae;
                // 添加或者更新区域信息
                if (isUpdate) {
                    ae = areaService.modifyArea(area);
                } else {
                    ae = areaService.addArea(area);
                }
                if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入区域信息!");
        }
        return map;
    }

    /**
     * 根据区域id获取区域信息
     *
     * @param areaId 区域id
     * @return model
     */
    @GetMapping("getareabyid/{areaId}")
    public Map<String, Object> getAreaById(@PathVariable("areaId") Integer areaId) {
        if (areaId != null && areaId > 0) {
            try {
                AreaExecution ae = areaService.getAreaById(areaId);
                if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                    map.put("area", ae.getArea());
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入区域id!");
        }
        return map;
    }
    /**
     * 根据区域id删除对应的区域信息
     *
     * @param areaId 区域id
     * @return model
     */
    @GetMapping("deleteareabyid/{areaId}")
    private Map<String, Object> removeHeadLine(@PathVariable("areaId") Integer areaId) {
        // 空值判断
        if (areaId != null && areaId > 0) {
            try {
                AreaExecution ae = areaService.deleteAreaById(areaId);
                if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入区域id!");
        }
        return map;
    }

}
