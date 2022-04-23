package com.ys.mail.annotation;

import java.lang.annotation.*;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-22 10:20
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalLockAnn {

    /**
     * key 方法名,必须以key=方法路由+“arg[0]”，多个参数可选arg[0,2]，表示选取指定下标的参数；
     *
     * @return 返回值
     */
    String key();

    /**
     * 过期时间，使用本地缓存可以忽略，如果使用redis做缓存就需要
     * 该参数范围：5~60
     *
     * @return 返回值
     */
    int expire() default 5;

    /**
     * 重复提交警告提示
     *
     * @return 返回值
     */
    String message() default "提交过于频繁，请%d秒后重试";
}
