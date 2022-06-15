package com.ys.mail.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.enums.AnsiColorEnum;
import com.ys.mail.exception.code.IErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理
 *
 * @author 070
 * @date 2020/12/17 21:34
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.profiles.active}")
    private String active;

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult<String> handler(ApiException e) {
        e.printStackTrace();
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public CommonResult<String> handler(BusinessException e, HttpServletResponse res) {
        String message = e.getMessage();
        // 打印简洁的业务异常
        String title = String.format("%s业务异常::%s", StringConstant.LEFT_ARROWS, message);
        String link = String.format("\n%s异常位置::%s", StringConstant.LEFT_ARROWS, e.getStackTrace()[1]);
        String content;
        if (active.contains(StringConstant.ENV_DEV)) {
            content = StringUtil.getColorContent(AnsiColorEnum.RED_BOLD, title + link);
            logger.warn(StringConstant.BLANK);
            System.out.println(content);
        } else {
            content = title + link;
            logger.warn(content);
        }

        // 返回结果
        IErrorCode errorCode = e.getErrorCode();
        if (errorCode != null) {
            return CommonResult.failed(errorCode, message);
        }
        return CommonResult.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<Object> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public CommonResult<Object> handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    /**
     * 全局异常捕捉处理 - todo -hwn
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Object> errorHandler(Exception ex) {
        CommonResult<Object> errorResult = CommonResult.failed("系统后台异常！请联系管理员。");
        //记录错误日志
        logger.error("捕获到程序后台出现错误，详细信息:\r\n" + ex.getMessage() + "\r\n");
        //记录堆栈信息
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            ex.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        logger.error(sw.toString());
        ex.printStackTrace();
        return errorResult;
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public CommonResult<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
        // String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        String message = e.getMessage();
        if (!StringUtils.isEmpty(message)) {
            message = message.substring(message.lastIndexOf(".") + 1);
        }
        return CommonResult.validateFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<String> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return CommonResult.errRequestMethod(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResult<String> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return CommonResult.errHttpMediaType(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResult<String> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return CommonResult.errMissingServlet(e.getMessage());
    }

    /**
     * json传参异常
     *
     * @param e 异常
     * @return 返回json格式
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return CommonResult.errNotReadable(e.getMessage());
    }

    /**
     * 数据库建立的索引出现重复报错
     *
     * @param e 异常
     * @return 返回提示
     */
    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResult<String> duplicateKeyException(DuplicateKeyException e) {
        return CommonResult.errDuplicateKey(e.getCause().getMessage());
    }

    /**
     * 自定义余额支付解密异常
     *
     * @param e 异常
     * @return 返回body
     */
    @ResponseBody
    @ExceptionHandler(BadPaddingException.class)
    public CommonResult<String> badPaddingException(BadPaddingException e) {
        return CommonResult.errBadPadding(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(JsonMappingException.class)
    public CommonResult<String> jsonMappingException(JsonMappingException e) {
        return CommonResult.failed(BlankUtil.isEmpty(e) ? null : e.getMessage());
    }
}
