package com.ys.mail.enums;

/**
 * @Desc 脱敏类型枚举，默认定义了几种常用的处理策略
 * @Author CRH
 * @Create 2022-03-01 18:28
 */
public enum SensitiveTypeEnum {
    /**
     * 自定义，不常用使用定义即可
     */
    CUSTOMER,
    /**
     * 姓名，只对两个字以上的名称生效，如 李小花 -> 李*花
     */
    NAME,
    /**
     * 姓氏，如 李小花 -> 李**
     */
    LAST_NAME,
    /**
     * 名称，如 李小花 -> **花
     */
    FIRST_NAME,
    /**
     * 身份证
     */
    ID_NUM,
    /**
     * 手机号码
     */
    PHONE_NUM
}
