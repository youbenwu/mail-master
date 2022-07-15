package com.ys.mail.aop;

import com.ys.mail.service.UmsUserBlacklistService;
import com.ys.mail.util.BlankUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Desc 黑名单处理器
 * @Author CRH
 * @Create 2022-01-30 13:19
 */
@Aspect
@Order(0)
@Component
public class BlackListPhoneAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListPhoneAspect.class);

    @Autowired
    private UmsUserBlacklistService blacklistService;

    @Pointcut("@annotation(com.ys.mail.annotation.BlackListPhone)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String blPhone = request.getParameter("phone");
        LOGGER.info("【APO拦截】- 校验手机号：{}", blPhone);
        // 黑名单手机号拦截
        if (BlankUtil.isNotEmpty(blPhone)) {
            blacklistService.checkPhone(blPhone);
        }
        return point.proceed();
    }
}
