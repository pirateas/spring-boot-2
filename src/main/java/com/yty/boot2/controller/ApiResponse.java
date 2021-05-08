package com.yty.boot2.controller;

/**
 * @author yangtianyu created on 2019/1/21
 */
public class ApiResponse<T> {

    /**
     * 成功的状态码
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * 未知错误的状态码
     */
    public static final int CODE_UNKNOWN = -1;
    /**
     * 成功的提示信息
     */
    public static final String MESSAGE_SUCCESS = "success";

    private int code = CODE_SUCCESS;
    private String message = MESSAGE_SUCCESS;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    /**
     * 创建一个成功的无数据的请求响应
     */
    public static <T> ApiResponse<T> ofSuccess() {
        return new ApiResponse<>();
    }

    /**
     * 创建一个成功的带数据的响应
     *
     * @param data 数据
     * @return 响应信息
     */
    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse<>(data);
    }

    /**
     * 创建一个失败的响应，给定状态码和错误信息
     *
     * @param message 可以阅读的错误信息
     * @return 响应信息
     */
    public static <T> ApiResponse<T> ofError(String message) {
        return new ApiResponse<>(CODE_UNKNOWN, message);
    }

    /**
     * 创建一个失败的响应，给定状态码和错误信息
     *
     * @param code  状态码，系统定义
     * @param message 可以阅读的错误信息
     * @return 响应信息
     */
    public static <T> ApiResponse<T> ofError(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    /***
     * 判断请求是否成功
     */
    public boolean succeed() {
        return this.code == CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public ApiResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
