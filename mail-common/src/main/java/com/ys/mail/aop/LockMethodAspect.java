package com.ys.mail.aop;

import cn.hutool.crypto.digest.DigestUtil;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IPUtil;
import com.ys.mail.util.ResultUtil;
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

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Desc 接口频繁请求拦截器
 * @Author CRH
 * @Create 2022-03-08 13:19
 */
@Aspect
@Order(1)
@Component
public class LockMethodAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockMethodAspect.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Pointcut("@annotation(com.ys.mail.annotation.LocalLockAnn)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        LOGGER.info("LocalLockAnn：Valid");
        StringBuilder sb = new StringBuilder();
        // 获取方法中的参数
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        // 获取注解中的参数
        LocalLockAnn localLockAnn = method.getAnnotation(LocalLockAnn.class);
        String key = localLockAnn.key();// 获取key值
        String message = localLockAnn.message();// 提示内容
        int expire = localLockAnn.expire();// 过期时间
        if (expire < 5 || expire > 60) expire = 5;// 范围限制
        // 拼接key，请求参数等
        sb.append(className)
          .append(".")
          .append(methodName)
          .append("()")
          .append(".")
          .append(IPUtil.getIpAddress());
        if (BlankUtil.isEmpty(key) && !key.contains("arg"))
            return CommonResult.failed(CommonResultCode.ERR_INTERFACE_PARAM);
        // 截取参数
        String[] splitKey = key.replaceAll("[A-Za-z:\\[\\]]", "").split(",");
        List<String> argList = ResultUtil.joinParamNameAndValue(paramNames, args, splitKey);
        sb.append(" ").append(argList);

        // 计算MD5摘要
        String md5Hex = DigestUtil.md5Hex(String.valueOf(sb));
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getLocalLockAnn()) + ":" + md5Hex;

        // 判断redis是否存在该key，存在即属于频繁
        Long latestExpire = redisService.getExpire(fullKey);
        if (latestExpire > 0) {
            message = String.format(message, latestExpire);
            return CommonResult.failed(CommonResultCode.FREQUENT_REQUEST, message, Boolean.FALSE);
        }
        // 存到redis中，设置自动过期时间，加上1秒损耗
        redisService.set(fullKey, null, expire + 1);
        return point.proceed();
    }

}
