package com.yty.boot2.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Joiner;
import com.yty.boot2.common.exception.BizException;
import com.yty.boot2.common.exception.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author yangtianyu
 */
@ControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Joiner JOINER =  Joiner.on(",");

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse onException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return ApiResponse.ofError(bizException.getErrorCode().getCode(), bizException.getErrorCode().getMessage());
        }
        // GET请求参数类型不正确，使用对象接收参数
        if (e instanceof BindException) {
            BindException exception = (BindException) e;
            List<FieldError> fieldErrors = exception.getFieldErrors();
            List<String> fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.toList());
            return ApiResponse.ofError(CommonErrorCode.PARAM_IS_ILLEGAL.getCode(), String.format(CommonErrorCode.PARAM_IS_ILLEGAL.getMessage(), JOINER.join(fields)));
        }
        // GET请求参数类型不正确，使用单个参数接收参数
        if (e instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) e;
            return ApiResponse.ofError(CommonErrorCode.PARAM_IS_ILLEGAL.getCode(), String.format(CommonErrorCode.PARAM_IS_ILLEGAL.getMessage(), exception.getName()));
        }
        // POST请求参数类型不正确
        if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) e;
            JsonMappingException cause = (JsonMappingException) exception.getCause();
            List<String> fields = cause.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.toList());
            return ApiResponse.ofError(CommonErrorCode.PARAM_IS_ILLEGAL.getCode(), String.format(CommonErrorCode.PARAM_IS_ILLEGAL.getMessage(), JOINER.join(fields)));
        }
        LOGGER.error(e.getMessage(), e);
        return ApiResponse.ofError(e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body instanceof ApiResponse) {
            return body;
        }
        return ApiResponse.ofSuccess(body);
    }
}
