package com.ys.mail.exception;


import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.IErrorCode;
import com.ys.mail.util.BlankUtil;

/**
 * 断言处理类
 *
 * @author ghdhj
 */
public class ApiAssert {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static <T> void fail(T t, IErrorCode errorCode) {
        if (BlankUtil.isEmpty(t)) {
            throw new ApiException(errorCode);
        }
    }

    public static <T> void fail(T t1, T t2, IErrorCode errorCode) {
        if (BlankUtil.isEmpty(t1) || BlankUtil.isEmpty(t2)) {
            throw new ApiException(BusinessErrorCode.NPE_PARAM);
        }
        if (!t1.equals(t2)) {
            throw new ApiException(errorCode);
        }
    }
}
