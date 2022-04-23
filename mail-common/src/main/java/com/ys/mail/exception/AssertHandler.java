package com.ys.mail.exception;


import com.ys.mail.exception.code.IErrorCode;

/**
 * @version V1.0
 * @Description: 断言处理类，用于抛出异常
 * @author: DT
 */
public class AssertHandler {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
