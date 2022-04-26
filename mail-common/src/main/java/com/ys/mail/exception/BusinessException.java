package com.ys.mail.exception;

import com.ys.mail.exception.code.IErrorCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author CRH
 * @date 2022-04-26 11:33
 * @since 1.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private IErrorCode errorCode;

    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
