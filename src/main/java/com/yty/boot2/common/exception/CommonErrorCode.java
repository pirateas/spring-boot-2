package com.yty.boot2.common.exception;

import java.io.Serializable;

/**
 * @author yangtianyu created on 2019/1/21
 */
public class CommonErrorCode implements Serializable {

    private static ErrorCode build(int code, String message) {
        return SystemErrorCodePrefix.COMMON.buildErrorCode(code, message);
    }

    public final static ErrorCode INVALID_CODE_IN_ERROR_CODE = build(1, "Error code is illegal! It must be in [1,10000)");

    /**
     * 参数不正确
     */
    public static final ErrorCode PARAM_IS_ILLEGAL = ErrorCode.of(10, "{0} is illegal.");
}
