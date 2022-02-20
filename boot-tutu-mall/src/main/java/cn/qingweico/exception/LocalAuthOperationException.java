package cn.qingweico.exception;

/**
 * @author 周庆伟
 * @date 2020/11/11
 */
public class LocalAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = -8260236137099919700L;

    public LocalAuthOperationException(String msg) {
        super(msg);
    }
}
