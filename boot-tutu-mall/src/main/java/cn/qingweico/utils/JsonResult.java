package cn.qingweico.utils;

import lombok.Data;

import java.util.Map;

/**
 * @author: 周庆伟
 * @date: 2021/11/4
 */
@Data
public class JsonResult {
    /**
     * 响应业务状态码
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 响应数据,可以是Object,也可以是List或Map等
     */
    private Object data;

    /**
     * 成功返回,带有数据的
     *
     * @param data Object
     * @return JsonResult
     */
    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    /**
     * 成功返回,不带有数据的,直接调用ok方法,data无须传入(其实就是null)
     *
     * @return JsonResult
     */
    public static JsonResult ok() {
        return new JsonResult(ResponseStatusEnum.SUCCESS);
    }

    public JsonResult(Object data) {
        this.status = ResponseStatusEnum.SUCCESS.status();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }


    /**
     * 错误返回,直接调用error方法即可,当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     *
     * @return JsonResult
     */
    public static JsonResult error() {
        return new JsonResult(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回,map中包含了多条错误信息,可以用于表单验证,把错误统一的全部返回出去
     *
     * @param map Map<Object, Object>
     * @return JsonResult
     */
    public static JsonResult errorMap(Map<?, ?> map) {
        return new JsonResult(ResponseStatusEnum.SYSTEM_ERROR, map);
    }

    /**
     * 错误返回,直接返回错误的消息
     *
     * @param msg String
     * @return JsonResult
     */
    public static JsonResult errorMsg(String msg) {
        return new JsonResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 自定义错误范围,需要传入一个自定义的枚举
     *
     * @param responseStatus ResponseStatusEnum
     * @return JsonResult
     */
    public static JsonResult errorCustom(ResponseStatusEnum responseStatus) {
        return new JsonResult(responseStatus);
    }

    public static JsonResult exception(ResponseStatusEnum responseStatus) {
        return new JsonResult(responseStatus);
    }

    public JsonResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }

    public JsonResult(ResponseStatusEnum responseStatus, Object data) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }

    public JsonResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }
}
