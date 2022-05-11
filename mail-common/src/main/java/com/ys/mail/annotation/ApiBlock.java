package com.ys.mail.annotation;

import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.util.RequestUtil;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 接口拦截，主要用于针对不同环境下重要接口
 *
 * @author CRH
 * @date 2022-03-24 11:23
 * @since 1.0
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiBlock {

    /**
     * 指定环境下进行拦截，默认拦截{test}
     */
    ENV env() default ENV.TEST;

    /**
     * 环境配置，需要与Profiles配置名称相同(小写即可)；可扩展修改
     */
    @NoArgsConstructor
    enum ENV {
        /**
         * 拦截所有
         */
        ALL,
        /**
         * 放行，不拦截
         */
        NONE,
        /**
         * 开发模式
         */
        DEV,
        /**
         * 测试模式，默认
         */
        TEST,
        /**
         * 生产模式
         */
        PROD,
        /**
         * 演示模式
         */
        DEMO
    }

    /**
     * 注解内部处理器
     */
    @Aspect
    @Component
    class ApiBlockAspect {

        private static final Logger LOGGER = LoggerFactory.getLogger(ApiBlockAspect.class);

        @Value("${spring.profiles.active}")
        private String active;

        @Pointcut("@annotation(ApiBlock)")
        public void pointCut() {}

        @Around(value = "pointCut()")
        public Object around(ProceedingJoinPoint point) throws Throwable {
            // 获取方法信息
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            // 获取自身注解信息
            ApiBlock apiBlock = method.getAnnotation(ApiBlock.class);
            ENV env = apiBlock.env();

            // 不对NONE进行拦截
            if (!ENV.NONE.equals(env)) {
                // 所有环境下都进行拦截 / 只拦截匹配的环境
                if (ENV.ALL.equals(env) || active.contains(env.name().toLowerCase())) {
                    LOGGER.warn("【ApiBlock】- [{}]环境下禁止请求该接口 - {}", env.name(), RequestUtil.getFullUrl());
                    return CommonResult.failed(CommonResultCode.ILLEGAL_REQUEST, Boolean.FALSE);
                }
            }

            // 允许访问
            return point.proceed();
        }

    }
}
