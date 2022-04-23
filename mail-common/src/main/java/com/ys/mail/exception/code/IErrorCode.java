package com.ys.mail.exception.code;

/**
 * @version V1.0
 * @Description:
 * @author: hezhidong
 */
public interface IErrorCode {
    /**
     * 状态码
     * @return 返回状态码
     */
    int getCode();

    /**
     * 描述.信息
     * @return 返回信息
     */
    String getMessage();
}
