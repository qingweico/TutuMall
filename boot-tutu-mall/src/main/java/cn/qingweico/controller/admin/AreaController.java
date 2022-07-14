package cn.qingweico.controller.admin;

import java.util.List;

import cn.qingweico.common.Result;
import cn.qingweico.dto.AreaExecution;
import cn.qingweico.entity.Area;
import cn.qingweico.enums.AreaStateEnum;
import cn.qingweico.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/11/14
 */
@Slf4j
@RestControllerAdvice
@RequestMapping("/a/area")
public class AreaController {
    @Resource
    private AreaService areaService;
    /**
     * 获取所有的区域信息
     *
     * @return Map
     */
    @GetMapping("/list")
    private Result listArea() {
        List<Area> list = areaService.getAreaList();
        return Result.ok(list);
    }

    /**
     * 添加或者修改区域信息
     *
     * @return Map
     */
    @PostMapping("/addOrUpdate")
    private Result addOrUpdateArea(@RequestBody Area area) {
        // 空值判断
        if (StringUtils.isNotEmpty(area.getName())) {
            AreaExecution ae;
            Long id = area.getId();
            // 添加或者更新区域信息
            if (id != null) {
                ae = areaService.modifyArea(area);
            } else {
                ae = areaService.addArea(area);
            }
            if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }

        }
        return Result.error();
    }

    /**
     * 根据区域id获取区域信息
     *
     * @param areaId 区域id
     * @return Result
     */
    @GetMapping("get")
    public Result getAreaById(@RequestParam Long areaId) {
        if (areaId != null) {
            AreaExecution ae = areaService.getAreaById(areaId);
            if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        }
        return Result.error();
    }

    /**
     * 根据区域id删除对应的区域信息
     *
     * @param areaId 区域id
     * @return model
     */
    @GetMapping("delete")
    private Result removeHeadLine(@RequestParam Long areaId) {
        // 空值判断
        if (areaId != null) {
            AreaExecution ae = areaService.deleteAreaById(areaId);
            if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        }
        return Result.error();
    }

}
