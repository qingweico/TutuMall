package cn.qingweico.common;

import cn.qingweico.utils.ResponseStatusEnum;
import lombok.Data;

import java.util.Map;

/**
 * @author zqw
 * @date 2022/7/12
 */
@Data
public class Result {
    /**
     * 响应业务状态码
     */
    private Integer code;

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
     * @return Result
     */
    public static Result ok(Object data) {
        return new Result(data);
    }

    /**
     * 成功返回,不带有数据的,直接调用ok方法,data无须传入(其实就是null)
     *
     * @return Result
     */
    public static Result ok() {
        return new Result(ResponseStatusEnum.SUCCESS);
    }

    public static Result ok(String msg) {
        return new Result(msg);
    }

    public Result(Object data) {
        this.code = ResponseStatusEnum.SUCCESS.code();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }


    /**
     * 错误返回,直接调用error方法即可,当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     *
     * @return Result
     */
    public static Result error() {
        return new Result(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回,map中包含了多条错误信息,可以用于表单验证,把错误统一的全部返回出去
     *
     * @param map Map<Object, Object>
     * @return Result
     */
    public static Result errorMap(Map<?, ?> map) {
        return new Result(ResponseStatusEnum.SYSTEM_ERROR, map);
    }

    /**
     * 错误返回,直接返回错误的消息
     *
     * @param msg String
     * @return Result
     */
    public static Result errorMsg(String msg) {
        return new Result(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 自定义错误范围,需要传入一个自定义的枚举
     *
     * @param responseStatus ResponseStatusEnum
     * @return Result
     */
    public static Result errorCustom(ResponseStatusEnum responseStatus) {
        return new Result(responseStatus);
    }

    public static Result exception(ResponseStatusEnum responseStatus) {
        return new Result(responseStatus);
    }

    public Result(ResponseStatusEnum responseStatus) {
        this.code = responseStatus.code();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }

    public Result(ResponseStatusEnum responseStatus, Object data) {
        this.code = responseStatus.code();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }

    public Result(ResponseStatusEnum responseStatus, String msg) {
        this.code = responseStatus.code();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    public Result(String msg) {
        this.code = ResponseStatusEnum.SUCCESS.code();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.msg = msg;
    }
}