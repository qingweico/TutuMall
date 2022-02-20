package cn.qingweico.exception;

/**
 * @author 周庆伟
 * @date 2020/10/16
 */

public class UserOperationException extends RuntimeException {

    public UserOperationException(String msg) {
        super(msg);
    }
}
