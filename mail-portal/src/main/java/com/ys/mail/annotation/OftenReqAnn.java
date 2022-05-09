package com.ys.mail.annotation;

import java.lang.annotation.*;

/**
 * 频繁请求一天限制次数
 *
 * @author ghdhj
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OftenReqAnn {

    /**
     * key 方法名,必须以key=方法路由+“arg[0]”，多个参数可选arg[0,2]，表示选取指定下标的参数；
     *
     * @return 返回值
     */
    String key();

    /**
     * num 次数限制,比如一天5次,10次等
     *
     * @return 返回值
     */
    int num() default 5;

    /**
     * expire 过期时间,以秒为单位,支持拓展,默认存入一天
     *
     * @return 返回值
     */
    long expire() default 86400;


    /**
     * 重复提交警告提示
     *
     * @return 返回值
     */
    String message() default "提交过于频繁，请%d秒后重试";
}
