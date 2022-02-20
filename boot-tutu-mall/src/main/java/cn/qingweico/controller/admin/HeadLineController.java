package cn.qingweico.controller.admin;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.qingweico.dao.HeadLineDao;
import cn.qingweico.dto.Constant4SuperAdmin;
import cn.qingweico.dto.HeadLineExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.HeadLine;
import cn.qingweico.enums.HeadLineStateEnum;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 周庆伟
 * @date 2020/11/14
 */

@RestControllerAdvice
@RequestMapping("/superadmin")
public class HeadLineController {

    private HeadLineService headLineService;

    private HeadLineDao headLineDao;

    private final Map<String, Object> map = new HashMap<>(5);

    @Autowired
    public void setHeadLineService(HeadLineService headLineService) {
        this.headLineService = headLineService;
    }

    @Autowired
    public void setHeadLineDao(HeadLineDao headLineDao) {
        this.headLineDao = headLineDao;
    }

    /**
     * 根据查询条件分页获取头条列表
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @GetMapping("/listheadlines")
    private Map<String, Object> listHeadLines(HttpServletRequest request) {
        List<HeadLine> list;
        try {
            // 若传入的查询条件里有可用状态,则依据可用状态检索
            int enableStatus = HttpServletRequestUtil.getInteger(request, "enableStatus");
            HeadLine headLine = new HeadLine();
            if (enableStatus > -1) {
                headLine.setEnableStatus(enableStatus);
            }
            list = headLineService.getHeadLineList(headLine);
            map.put(Constant4SuperAdmin.PAGE_SIZE, list);
            map.put(Constant4SuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        return map;
    }

    /**
     * 添加头条信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @PostMapping("/addormodifyheadline")
    private Map<String, Object> addOrModifyHeadLine(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Boolean isModify = (Boolean) params.get("isModify");
        ObjectMapper mapper = new ObjectMapper();
        HeadLine headLine;
        // 接收并转化相应的参数,包括头条信息以及图片信息
        String headLineString = (String) params.get("headLineString");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        try {
            headLine = mapper.readValue(headLineString, HeadLine.class);
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }

        try {
            if (multipartResolver.isMultipart(request)) {
                // MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) params.get("headLineImage");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                    thumbnail.setImageForm("THUMBNAIL");
                }
            }
        } catch (Exception e) {
            map.put("success", false);
            return map;
        }
        // 空值判断
        if (headLine != null) {
            try {
                // decode可能有中文的地方
                headLine.setLineName(
                        (headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
                headLine.setLineLink(
                        (headLine.getLineLink() == null) ? null : URLDecoder.decode(headLine.getLineLink(), "UTF-8"));
                // 添加或者修改头条信息
                HeadLineExecution ae;
                if (isModify) {
                    ae = headLineService.modifyHeadLine(headLine, thumbnail);
                } else {
                    ae = headLineService.addHeadLine(headLine, thumbnail);
                }
                if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ae.getStateInfo());
                }
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入头条信息!");
        }
        return map;
    }

    /**
     * 删除单个头条信息
     *
     * @param headLineId 头条id
     * @return model
     */
    @GetMapping("/removeheadline/{headLineId}")
    private Map<String, Object> removeHeadLine(@PathVariable("headLineId") Long headLineId) {
        // 空值判断
        if (headLineId != null && headLineId > 0) {
            try {
                // 根据传入的Id删除对应的头条
                HeadLineExecution ae = headLineService.removeHeadLine(headLineId);
                if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
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
            map.put("errorMessage", "请输入头条信息!");
        }
        return map;
    }

    /**
     * 批量删除头条信息
     *
     * @param headLineIdListString headLineIdListString
     * @return Map
     */
    @PostMapping("/removeheadlines")
    private Map<String, Object> removeHeadLines(String headLineIdListString) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> headLineIdList = null;
        try {
            // 将前端传入的Id列表字符串转换成List<Integer>
            headLineIdList = mapper.readValue(headLineIdListString, javaType);
        } catch (Exception e) {
            map.put("success", false);
        }
        // 空值判断
        if (headLineIdList != null && headLineIdList.size() > 0) {
            try {
                // 根据传入的头条Id列表批量删除头条信息
                HeadLineExecution ae = headLineService.removeHeadLineList(headLineIdList);
                if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
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
            map.put("errorMessage", "请输入头条信息!");
        }
        return map;
    }

    /**
     * 根据头条id查询头条的信息
     * @param headLineId 头条id
     * @return HeadLine
     */
    @GetMapping("/getheadlinebyid/{headLineId}")
    public HeadLine getHeadLineById(@PathVariable("headLineId") Integer headLineId) {
        return headLineDao.queryHeadLineById(headLineId);
    }

}
