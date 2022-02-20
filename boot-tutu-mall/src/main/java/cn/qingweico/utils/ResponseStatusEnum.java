package cn.qingweico.utils;

/**
 * @author: 周庆伟
 * @date: 2021/11/4
 */
public enum ResponseStatusEnum {

    // 200
    SUCCESS(200, true, "操作成功!"),
    LOGIN_SUCCESS(200, true, "登陆成功!"),
    DELETE_SUCCESS(200, true, "删除成功!"),
    EXCHANGE_SUCCESS(200, true, "兑换成功!"),
    REGISTER_SHOP_SUCCESS(200, true, "注册店铺成功, 等待审核中!"),
    UPDATE_SUCCESS(200, true, "修改店铺信息成功!"),

    // 50x
    FAILED(500, false, "操作失败!"),
    UN_LOGIN(501, false, "请登录后再继续操作!"),
    UN_AUTH(502, false, "无操作权限!"),
    AUTH_FAIL(503, false, "用户名或密码错误!"),
    AUTH_INFO_NULL(504, false, "用户名或密码不能为空!"),
    CHOOSE_AWARD(505, false, "请选择领取的奖品!"),
    VERIFICATION_CODE_ERROR(506, false, "验证码错误!"),
    DIFFERENT_ACCOUNT(507, false, "输入的帐号非本次登录的帐号!"),
    FORBIDDEN_ACCOUNT(508, false, "该用户已被禁用"),
    CHECK_INFO(509, false, "请检查您输入的信息!"),


    // 系统错误,未预期的错误 55x
    SYSTEM_ERROR(555, false, "系统繁忙,请稍后再试!"),
    SYSTEM_OPERATION_ERROR(556, false, "操作失败, 请重试或联系管理员"),
    SYSTEM_RESPONSE_NO_INFO(557, false, ""),
    SYSTEM_REQUEST_REFUSE(588, false, "请求系统过于繁忙,请稍后再试!"),
    REQUEST_PARAM_ERROR(600, false, "请求参数错误!");


    /**
     * 响应业务状态
     */
    private final Integer status;
    /**
     * 调用是否成功
     */
    private final Boolean success;
    /**
     * 响应消息,可以为成功或者失败的消息
     */
    private final String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }

    public Boolean success() {
        return success;
    }

    public String msg() {
        return msg;
    }
}
