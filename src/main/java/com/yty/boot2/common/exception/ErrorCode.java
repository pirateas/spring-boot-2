package com.yty.boot2.common.exception;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 错误码
 *
 * @author yangtianyu created on 2019/1/21
 */
public class ErrorCode implements Serializable {

    public final static int PARAMETER_ERROR_CODE = -1;
    public final static int RUNTIME_ERROR_CODE = -2;

    private final int code;
    private final String message;
    private Object data;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode of(int code, String message) {
        return new ErrorCode(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage(Object... objects) {
        if (Objects.nonNull(objects) && objects.length > 0) {
            return MessageFormat.format(message, objects);
        } else {
            return message;
        }
    }

    public String getMessage() {
        return message;
    }

    public void check(boolean expect, Object... objects) {
        if (expect) {
            return;
        }
        throwBizException(objects);
    }

    public void throwBizException(Object... objects) {
        throw new BizException(errorCode(objects));
    }

    public ErrorCode errorCode(Object... objects) {
        return ErrorCode.of(code, getMessage(objects));
    }

    public void checkNull(Object obj, Object... objects) {
        check(Objects.nonNull(obj), objects);
    }

    public void checkBlank(String str, Object... objects) {
        check(StringUtils.isNotBlank(str), objects);
    }

    public void checkEmpty(Collection collections, Object... objects) {
        check(CollectionUtils.isNotEmpty(collections), objects);
    }

    public void checkEmpty(Map map, Object... objects) {
        check(MapUtils.isNotEmpty(map), objects);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
