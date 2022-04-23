package com.ys.mail.security.component;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.security.util.Test;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义返回结果：未登录或登录过期
 * @author ghdhj
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    /**
     * 自定义异常
     */
    private static final String ERROR_PATH_VARIABLE = "/error";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        if(request.getRequestURI().equals(ERROR_PATH_VARIABLE)){
            response.getWriter().println(JSONUtil.parse(CommonResult.urlFailed(new Test())));
        }else{
            response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(new Test())));
        }
        LOGGER.debug("异常{}",authException.getMessage());
        response.getWriter().flush();
    }
}
