package com.ys.mail.aop;

import com.ys.mail.annotation.OftenReqAnn;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ghdhj
 */
@Aspect
@Order(2)
@Component
public class ReqMethodAspect {

    private static final Logger log = LoggerFactory.getLogger(ReqMethodAspect.class);

    private static final String ARG = "arg";

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Pointcut("@annotation(com.ys.mail.annotation.OftenReqAnn)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("OftenReqAnn：Valid");

        StringBuilder sb = new StringBuilder().append("mail:method:");
        MethodSignature signature = (MethodSignature) point.getSignature();
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        OftenReqAnn oftenReqAnn = signature.getMethod().getAnnotation(OftenReqAnn.class);
        String key = oftenReqAnn.key();
        int num = oftenReqAnn.num();
        long expire = oftenReqAnn.expire();

        //ip userId 方法名字
        String key3 = sb.append(className)
                .append(":")
                .append(methodName)
                .append(":")
                .append(UserUtil.getCurrentUser().getUserId()).toString();
        Integer ite = (Integer) redisService.get(key3);
        if (BlankUtil.isEmpty(key) || !key.contains(ARG)) {
            throw new ApiException(BusinessErrorCode.NPE_PARAM);
        }
        if (BlankUtil.isEmpty(ite)) {
            redisService.set(key3, num, expire);
        } else {
            if (ite > NumberUtils.INTEGER_ZERO) {
                redisService.set(key3, ite - NumberUtils.INTEGER_ONE, expire);
            } else {
                throw new ApiException(String.format(oftenReqAnn.message(), expire));
            }
        }
        return point.proceed();
    }
}
