package com.ys.mail.exception.code;

/**
 * @Description:
 * @author: hezhidong
 */
public class RuntimeErrorCode implements IErrorCode {

    private int code;

    private String message;

    public RuntimeErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
