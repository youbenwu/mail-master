package com.ys.mail.util;

import com.ys.mail.model.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Desc 请求上下文工具类
 * @Author CRH
 * @Create 2022-03-24 14:46
 */
public class RequestUtil {

    /**
     * 获取Spring上下文中的Request对象
     *
     * @return Request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }

    /**
     * 获取请求方法，如GET、POST等
     *
     * @return 方法头
     */
    public static String getMethod() {
        return getRequest().getMethod();
    }

    /**
     * 获取请求的路径
     *
     * @return URI
     */
    public static String getRequestUri() {
        return getRequest().getRequestURI();
    }

    /**
     * 获取完整的URI，如：GET:/api/xxx
     *
     * @return 完整的URI
     */
    public static String getFullUrl() {
        return String.format("%s:%s", getMethod(), getRequestUri());
    }

    /**
     * 从参数中获取完整的URI，如：GET:/api/xxx
     *
     * @param request 将请求对象传过来
     * @return 完整的URI
     */
    public static String getFullUrl(HttpServletRequest request) {
        return String.format("%s:%s", request.getMethod(), request.getRequestURI());
    }

    /**
     * 获取请求头信息
     *
     * @param s 属性名称
     * @return 属性值
     */
    public static String getHeader(String s) {
        return getRequest().getHeader(s);
    }

    /***
     * 设置HTTP响应状态码
     *
     * @param res 响应对象
     * @param result 结果
     */
    public static void setStatus(HttpServletResponse res, CommonResult<?> result) {
        if (result.getCode() != HttpStatus.OK.value()) {
            res.setStatus((int) result.getCode());
        }
    }
}
