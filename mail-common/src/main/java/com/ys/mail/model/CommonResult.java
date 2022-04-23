package com.ys.mail.model;

import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.exception.code.IErrorCode;

/**
 * @version V1.0
 * @Description: 通用返回对象
 * @author: DT
 */
public class CommonResult<T> {
    /**
     * 状态码
     */
    private long code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据封装
     */
    private T data;

    public CommonResult() {

    }


    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<T>(CommonResultCode.SUCCESS.getCode(), message, data);
    }


    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> CommonResult<T> failed(IErrorCode errorCode, T data) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message, T data) {
        return new CommonResult<T>(errorCode.getCode(), message, data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(CommonResultCode.FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> failed(String message, T data) {
        return new CommonResult<T>(CommonResultCode.FAILED.getCode(), message, data);
    }

    public static <T> CommonResult<T> failed(T data) {
        return new CommonResult<T>(CommonResultCode.FAILED.getCode(), CommonResultCode.FAILED.getMessage(), data);
    }

    public static <T> CommonResult<T> errRequestMethod(String message) {
        return new CommonResult<T>(CommonResultCode.ERR_REQUEST_METHOD.getCode(), message, null);
    }

    public static <T> CommonResult<T> errHttpMediaType(String message) {
        return new CommonResult<T>(CommonResultCode.ERR_HTTP_MEDIA_TYPE.getCode(), message, null);
    }

    public static <T> CommonResult<T> errMissingServlet(String message) {
        return new CommonResult<T>(CommonResultCode.ERR_MISSING_SERVLET.getCode(), message, null);
    }

    public static <T> CommonResult<T> errNotReadable(String message) {
        return new CommonResult<T>(CommonResultCode.ERR_NOT_READABLE.getCode(), message, null);
    }

    public static CommonResult<String> errDuplicateKey(String message) {
        return new CommonResult<>(CommonResultCode.ERR_NOT_READABLE.getCode(), message, null);
    }

    public static CommonResult<String> errBadPadding(String message) {
        return new CommonResult<>(CommonResultCode.ERR_BAD_PADDING.getCode(), CommonResultCode.ERR_BAD_PADDING.getMessage(), null);
    }


    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(CommonResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {

        return new CommonResult<T>(CommonResultCode.UNAUTHORIZED.getCode(), CommonResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage(), data);
    }

    /**
     * URL参数验证失败返回结果
     *
     * @param data 提示信息
     */
    public static <T> CommonResult<T> urlFailed(T data) {
        return new CommonResult<T>(CommonResultCode.URL_FAILED.getCode(), CommonResultCode.URL_FAILED.getMessage(), data);
    }

}
