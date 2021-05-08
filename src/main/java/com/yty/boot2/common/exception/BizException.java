package com.yty.boot2.common.exception;

/**
 * 业务异常
 *
 * @author yangtianyu created on 2019/1/21
 */
public class BizException extends RuntimeException {

    private final ErrorCode errorCode;
    private Object data;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BizException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }
}
