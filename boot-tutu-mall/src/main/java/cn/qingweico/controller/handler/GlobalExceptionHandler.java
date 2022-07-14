package cn.qingweico.controller.handler;

import cn.qingweico.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * -------------- 全局处理异常 --------------
 *
 * @author zqw
 * @date 2020/11/13
 */
@Slf4j
@RestControllerAdvice(annotations = {Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        return Result.errorMsg(e.getMessage());
    }
}
