package com.ys.mail.aop;

import com.ys.mail.annotation.ProductLog;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.service.SysProductLogService;
import com.ys.mail.util.IPUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-19 17:03
 */
@Aspect
@Component
public class ProductLogAspect {

    @Autowired
    private SysProductLogService productLogService;

    @Pointcut("@annotation(com.ys.mail.annotation.ProductLog)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 执行时长
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveProductLog(point, time);
        return result;
    }

    private void saveProductLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysProductLog productLog = new SysProductLog();

        // 获取注解上的操作描述
        ProductLog productLogAnnotation = method.getAnnotation(ProductLog.class);

        if (productLogAnnotation != null) {
            productLog.setOperation(productLogAnnotation.value());
        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        productLog.setMethod(className + "." + methodName + "()");

        // 请求的方法参数
        Object[] args = joinPoint.getArgs();

        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        Integer integerZero = NumberUtils.INTEGER_ZERO;
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            if(args.length > integerZero){
                Long arg = (Long) args[integerZero];
                productLog.setProductId(arg);
            }
            for (int i = 0; i < args.length; i++) {
                params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
            }
            productLog.setParams(params.toString());
        }

        productLog.setIp(IPUtil.getIpAddress());

        // 获取当前登录用户名
        UmsUser currentUser = UserUtil.getCurrentUser();
        productLog.setUserId(currentUser.getUserId());

        productLog.setTime((int) time);
        productLog.setProductLogId(IdWorker.generateId());
        productLogService.save(productLog);
    }

}
