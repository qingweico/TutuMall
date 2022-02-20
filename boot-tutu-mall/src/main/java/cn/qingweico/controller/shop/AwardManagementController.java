package cn.qingweico.controller.shop;

import cn.qingweico.dto.AwardExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.AwardStateEnum;
import cn.qingweico.service.AwardService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */

@Slf4j
@RestController
@RequestMapping("/shop")
public class AwardManagementController {

    @Resource
    private AwardService awardService;

    /**
     * 通过店铺id获取该店铺下的奖品列表
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/listAwardsByShop")
    private JsonResult listAwardsByShop(HttpServletRequest request) {
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session里获取shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            // 判断查询条件里面是否传入奖品名,有则模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            // 拼接查询条件
            Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(), awardName);
            // 根据查询条件分页获取奖品列表即总数
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("awardList", ae.getAwardList());
            map.put("count", ae.getCount());
            return JsonResult.ok(map);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 通过商品id获取奖品信息
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @GetMapping("/getAwardById")
    private JsonResult getAwardById(HttpServletRequest request) {
        // 从request里边获取前端传递过来的awardId
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        // 空值判断
        if (awardId > -1) {
            // 根据传入的Id获取奖品信息并返回
            Award award = awardService.getAwardById(awardId);
            return JsonResult.ok(award);
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 添加或者修改奖品信息
     *
     * @param request HttpServletRequest
     * @return JsonResult
     */
    @PostMapping("/addOrModifyAward")
    private JsonResult addOrModifyAward(HttpServletRequest request) {
        // 验证码校验
        if (!HttpServletRequestUtil.checkStatusChange(request) && !CodeUtil.checkVerificationCode(request)) {
            return JsonResult.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        // 接收前端参数的变量的初始化,包括奖品, 缩略图
        ObjectMapper mapper = new ObjectMapper();
        Award award;
        String awardStr = HttpServletRequestUtil.getString(request, "awardString");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new
                CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile)
                        multipartRequest.getFile("thumbnail");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                    // 设置奖品图片形式
                    thumbnail.setImageForm("THUMBNAIL");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        try {
            // 将前端传入的awardString转换成奖品对象
            award = mapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        // 判断是否为修改操作还是添加操作
        boolean isModify = HttpServletRequestUtil.getBoolean(request, "isModify");
        if (isModify || HttpServletRequestUtil.checkStatusChange(request)) {
            // 修改
            return modifyAward(request, award, thumbnail);
        } else {
            // 新增
            return addAward(request, award, thumbnail);
        }
    }

    /**
     * 新增奖品
     *
     * @param request   HttpServletRequest
     * @param award     奖品实体类
     * @param thumbnail ImageHolder
     * @return JsonResult
     */
    public JsonResult addAward(HttpServletRequest request, Award award, ImageHolder thumbnail) {
        if (award != null && thumbnail != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                // 添加award
                AwardExecution awardExecution = awardService.addAward(award, thumbnail);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok();
                } else {
                    return JsonResult.errorMsg(awardExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 修改奖品信息
     *
     * @param request   HttpServletRequest
     * @param award     奖品实体类
     * @param thumbnail ImageHolder
     * @return JsonResult
     */
    public JsonResult modifyAward(HttpServletRequest request, Award award, ImageHolder thumbnail) {
        if (award != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution awardExecution = awardService.modifyAward(award, thumbnail);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()) {
                    return JsonResult.ok(awardExecution.getAward().getEnableStatus());
                } else {
                    return JsonResult.errorMsg(awardExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }

        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.CHECK_INFO);
        }
    }

    /**
     * 封装商品查询条件到award实例中
     *
     * @param shopId    店铺id
     * @param awardName 奖品名称
     * @return Award
     */
    private Award compactAwardCondition4Search(int shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
}
