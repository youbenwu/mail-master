package com.ys.mail.exception.code;

/**
 * @version V1.0
 * @Description:
 * @author: hezhidong
 */
public interface IErrorCode {
    /**
     * 状态码
     *
     * @return 返回状态码
     */
    int getCode();

    /**
     * 描述.信息
     *
     * @return 返回信息
     */
    String getMessage();

    /**
     * 获取格式化的描述，信息，如：<br/>
     * - 不能小于%d天 -> 不能小于7天 <br/>
     * - 单笔最大限额%s -> 单笔最大限额5万 <br/>
     *
     * @param args 可变长参数
     * @return 格式化后的信息
     */
    default String getMessage(Object... args) {
        String message = this.getMessage();
        return String.format(message, args);
    }
}
