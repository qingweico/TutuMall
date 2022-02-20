package cn.qingweico.exception;

/**
 * @author 周庆伟
 * @date 2020/09/26
 */
public class ProductOperationException extends RuntimeException {
    public ProductOperationException(String message) {
        super(message);
    }
}
