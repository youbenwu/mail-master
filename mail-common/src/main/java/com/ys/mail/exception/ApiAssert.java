package com.ys.mail.exception;

import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.IErrorCode;
import com.ys.mail.util.BlankUtil;

import java.util.Objects;

/**
 * 断言处理类
 *
 * @author 007
 */
public class ApiAssert {

    /**
     * 直接抛出
     *
     * @param message 内容
     */
    public static void fail(String message) {
        throw new ApiException(message);
    }

    /**
     * 直接抛出
     *
     * @param errorCode 错误码
     */
    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    /**
     * 当数据有值时抛出异常
     *
     * @param t         数据对象
     * @param errorCode 异常码
     */
    public static <T> void haveValue(T t, IErrorCode errorCode) {
        if (BlankUtil.isNotEmpty(t)) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 当数据无值时抛出异常
     *
     * @param t         数据对象
     * @param errorCode 异常码
     */
    public static <T> void noValue(T t, IErrorCode errorCode) {
        if (BlankUtil.isEmpty(t)) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * {t1}与{t2}相等则抛出异常
     *
     * @param t1        数据t1
     * @param t2        数据t2
     * @param errorCode 异常码
     */
    public static <T> void eq(T t1, T t2, IErrorCode errorCode) {
        if (BlankUtil.isEmpty(t1) || BlankUtil.isEmpty(t2)) {
            throw new BusinessException(BusinessErrorCode.NPE_PARAM);
        }
        if (Objects.equals(t1, t2)) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * {t1}与{t2}不相等则抛出异常
     *
     * @param t1        数据t1
     * @param t2        数据t2
     * @param errorCode 异常码
     */
    public static <T> void noEq(T t1, T t2, IErrorCode errorCode) {
        if (BlankUtil.isEmpty(t1) || BlankUtil.isEmpty(t2)) {
            throw new BusinessException(BusinessErrorCode.NPE_PARAM);
        }
        if (!Objects.equals(t1, t2)) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 如果为true则抛出异常
     *
     * @param condition 条件，如 3 > 2 等表达式
     * @param errorCode 异常码
     */
    public static <T> void isTrue(boolean condition, IErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 如果为false则抛出异常
     *
     * @param condition 条件，如 3 > 5 等表达式
     * @param errorCode 异常码
     */
    public static <T> void isFalse(boolean condition, IErrorCode errorCode) {
        if (!condition) {
            throw new BusinessException(errorCode);
        }
    }

}
