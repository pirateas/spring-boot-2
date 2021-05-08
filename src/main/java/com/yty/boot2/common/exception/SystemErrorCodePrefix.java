package com.yty.boot2.common.exception;

import com.google.common.collect.Range;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * @author yangtianyu created on 2019/1/21
 */
public enum SystemErrorCodePrefix implements Serializable {

    /**
     * 通用服务
     */
    COMMON(10);

    private final int code;

    SystemErrorCodePrefix(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * 有效的代码区间
     */
    private final static Range<Integer> VALID_CODE_RANGE = Range.closedOpen(1, 100000);

    /**
     * SystemErrorCodePrefix.COMMON.buildErrorCode(1, "初始化错误");
     *
     * ErrorCode.code = 100001
     *
     * @param newValue it must be in [1, 100000), for example 1. The prefix is code.
     * @param message
     * @return
     */
    public ErrorCode buildErrorCode(int newValue, String message) {
//        ConditionUtils.checkArgument(VALID_CODE_RANGE.contains(newValue), CommonErrorCode.INVALID_CODE_IN_ERROR_CODE);
//        ConditionUtils.checkBlank(message, CommonErrorCode.INVALID_MESSAGE_IN_ERROR_CODE);
        String str = String.format("%04d", newValue);
        return ErrorCode.of(NumberUtils.toInt(code + str), message);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
