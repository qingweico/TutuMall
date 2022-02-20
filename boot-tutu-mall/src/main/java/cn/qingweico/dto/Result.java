package cn.qingweico.dto;

/**
 * @author 周庆伟
 * @date 2020/09/19
 */
public class Result<T> {
    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 成功时返回的数据
     */
    private T data;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 错误代码
     */
    private int errorCode;

    /**
     * 成功时的构造器
     *
     * @param success 成功标志
     * @param data    成功时返回的数据
     */
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 失败时返回的构造器
     *
     * @param success      成功标志
     * @param errorCode    错误代码
     * @param errorMessage 错误信息
     */
    public Result(boolean success, int errorCode, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
