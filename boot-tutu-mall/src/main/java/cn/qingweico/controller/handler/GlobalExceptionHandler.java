package cn.qingweico.controller.handler;

import cn.qingweico.exception.ProductOperationException;
import cn.qingweico.exception.ShopOperationException;
import cn.qingweico.utils.JsonResult;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局处理异常
 *
 * @author 周庆伟
 * @date 2020/11/13
 */
@Slf4j
@RestControllerAdvice(annotations = {Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public JsonResult handle(Exception e) {
        if (e instanceof ShopOperationException) {
            return JsonResult.errorMsg(e.getMessage());
        } else if (e instanceof ProductOperationException) {
            return JsonResult.errorMsg(e.getMessage());
        } else {
            log.error("系统出现异常:{}", e.getMessage());
            return JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
    }
}
