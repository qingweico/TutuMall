package cn.qingweico.controller.shop;

import cn.qingweico.common.Result;
import cn.qingweico.dto.AwardExecution;
import cn.qingweico.dto.ImageHolder;
import cn.qingweico.entity.Award;
import cn.qingweico.entity.Shop;
import cn.qingweico.enums.AwardStateEnum;
import cn.qingweico.service.AwardService;
import cn.qingweico.utils.CodeUtil;
import cn.qingweico.utils.HttpServletRequestUtil;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * -------------- 店铺奖品 --------------
 * @author zqw
 * @date 2020/10/15
 */

@Slf4j
@RestController
@RequestMapping("/shop/award")
public class ShopAwardController {

    @Resource
    private AwardService awardService;

    /**
     * 通过店铺id获取该店铺下的奖品列表
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/list")
    private Result listAwardsByShop(HttpServletRequest request) {
        // 获取分页信息
        int page = HttpServletRequestUtil.getInteger(request, "page");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // 从session里获取shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验
        if ((page > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getId() != null)) {
            // 判断查询条件里面是否传入奖品名,有则模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            // 拼接查询条件
            Award awardCondition = compactAwardCondition(currentShop.getId(), awardName);
            // 根据查询条件分页获取奖品列表即总数
            AwardExecution ae = awardService.getAwardList(awardCondition, page, pageSize);
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("awardList", ae.getAwardList());
            map.put("count", ae.getCount());
            return Result.ok(map);
        }
        log.info("page: {}, pageSize: {}, currentShop: {}", page, pageSize, currentShop);
        return Result.error();
    }

    /**
     * 通过商品id获取奖品信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/get")
    private Result getAwardById(HttpServletRequest request) {
        // 从request里边获取前端传递过来的awardId
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        // 空值判断
        if (awardId > -1) {
            // 根据传入的Id获取奖品信息并返回
            Award award = awardService.getAwardById(awardId);
            return Result.ok(award);
        }
        log.error("awardId: {}", awardId);
        return Result.error();
    }

    /**
     * 添加或者修改奖品信息
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/addOrModify")
    private Result addOrModifyAward(HttpServletRequest request, @RequestBody Award award) throws IOException {
        // 验证码校验
        if (!HttpServletRequestUtil.checkStatusChange(request) && !CodeUtil.checkVerificationCode(request)) {
            return Result.errorCustom(ResponseStatusEnum.VERIFICATION_CODE_ERROR);
        }
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new
                CommonsMultipartResolver(request.getSession().getServletContext());
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
        // 判断是否为修改操作还是添加操作
        if (award.getId() != null || HttpServletRequestUtil.checkStatusChange(request)) {
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
     * @return Result
     */
    public Result addAward(HttpServletRequest request, Award award, ImageHolder thumbnail) {
        if (award != null && thumbnail != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getId());
            // 添加award
            AwardExecution awardExecution = awardService.addAward(award, thumbnail);
            if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()) {
                return Result.ok();
            } else {
                return Result.errorMsg(awardExecution.getStateInfo());
            }
        }
        return Result.error();
    }

    /**
     * 修改奖品信息
     *
     * @param request   HttpServletRequest
     * @param award     奖品实体类
     * @param thumbnail ImageHolder
     * @return Result
     */
    public Result modifyAward(HttpServletRequest request, Award award, ImageHolder thumbnail) {
        if (award != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getId());
            AwardExecution awardExecution = awardService.modifyAward(award, thumbnail);
            if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()) {
                return Result.ok(awardExecution.getAward().getEnableStatus());
            } else {
                return Result.errorMsg(awardExecution.getStateInfo());
            }
        }
        return Result.error();
    }

    /**
     * 封装商品查询条件到award实例中
     *
     * @param shopId    店铺id
     * @param awardName 奖品名称
     * @return Award
     */
    private Award compactAwardCondition(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setName(awardName);
        }
        return awardCondition;
    }
}
