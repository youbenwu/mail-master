package com.ys.mail.exception;

import com.ys.mail.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version V1.0
 * @Description: 全局异常处理
 * @author: hezhidong
 */
@ControllerAdvice
@Slf4j
public class GlobalExHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handler(ApiException e) {
    	log.error("api异常",e);
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }
}
