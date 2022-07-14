package cn.qingweico.controller.admin;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.qingweico.common.Result;
import cn.qingweico.dao.HeadLineDao;
import cn.qingweico.dto.HeadLineExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.HeadLine;
import cn.qingweico.enums.HeadLineStateEnum;
import cn.qingweico.service.HeadLineService;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author zqw
 * @date 2020/11/14
 */

@RestControllerAdvice
@RequestMapping("/a/headline")
public class HeadlineController {

    @Resource
    private HeadLineService headLineService;

    @Resource
    private HeadLineDao headLineDao;

    /**
     * 根据查询条件分页获取头条列表
     *
     * @param request HttpServletRequest
     * @return Result<?>
     */
    @GetMapping("/list")
    private Result listHeadLines(HttpServletRequest request) {
        List<HeadLine> list;
        // 若传入的查询条件里有可用状态,则依据可用状态检索
        int enableStatus = HttpServletRequestUtil.getInteger(request, "enableStatus");
        HeadLine headLine = new HeadLine();
        if (enableStatus > -1) {
            headLine.setEnableStatus(enableStatus);
        }
        list = headLineService.getHeadLineList(headLine);
        return Result.ok(list);
    }

    /**
     * 添加头条信息
     *
     * @param request HttpServletRequest
     * @return Map
     */
    @PostMapping("/addOrModify")
    private Result addOrModifyHeadLine(@RequestBody HeadLine headLine, HttpServletRequest request) throws IOException {
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            //  取出缩略图并构建ImageHolder对象
            CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("headLineImage");
            if (thumbnailFile != null) {
                thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                thumbnail.setImageForm("THUMBNAIL");
            }
        }
        // 添加或者修改头条信息
        HeadLineExecution ae;
        Long id = headLine.getId();
        if (id != null) {
            ae = headLineService.modifyHeadLine(headLine, thumbnail);
        } else {
            ae = headLineService.addHeadLine(headLine, thumbnail);
        }
        if (ae.getState() == HeadLineStateEnum.DELETE_SUCCESS.getState()) {
            return Result.ok(ae.getStateInfo());
        } else {
            return Result.errorMsg(ae.getStateInfo());
        }

    }

    /**
     * 删除单个头条信息
     *
     * @param id 头条id
     * @return model
     */
    @GetMapping("/delete")
    private Result removeHeadLine(@RequestParam Long id) {
        if (id != null) {
            HeadLineExecution ae = headLineService.deleteHeadlineById(id);
            if (ae.getState() == HeadLineStateEnum.DELETE_SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        }
        return Result.error();

    }

    /**
     * 批量删除头条信息
     *
     * @param headLineIds headLineIds
     * @return Result
     */
    @PostMapping("/deletes")
    private Result removeHeadLines(String headLineIds) {
        List<Long> headLineIdList = JsonUtils.jsonToList(headLineIds, Long.class);
        // 空值判断
        if (headLineIdList != null && headLineIdList.size() > 0) {
            HeadLineExecution ae = headLineService.removeHeadLineList(headLineIdList);
            if (ae.getState() == HeadLineStateEnum.DELETE_SUCCESS.getState()) {
                return Result.ok(ae.getStateInfo());
            } else {
                return Result.errorMsg(ae.getStateInfo());
            }
        } else {
            return Result.error();
        }
    }

    /**
     * 根据头条id查询头条的信息
     *
     * @param headLineId 头条id
     * @return HeadLine
     */
    @GetMapping("/get")
    public HeadLine getHeadLineById(@RequestParam Long headLineId) {
        return headLineDao.queryHeadLineById(headLineId);
    }
}
