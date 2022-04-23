package com.ys.mail.exception.code;

import lombok.Getter;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 16:40
 */
@Getter
public enum RongCloudErrorCode implements IErrorCode {

    //invoke rongcloud server error
    INVOKE_RONG_CLOUD_SERVER_ERROR(2000, "RongCloud Server API Error: "),


    ;

    private int code;

    private String message;

    RongCloudErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
