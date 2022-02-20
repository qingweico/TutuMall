package cn.qingweico.exception;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */
public class WeChatAuthOperationException extends RuntimeException {
    public WeChatAuthOperationException(String message) {
        super(message);
    }
}
