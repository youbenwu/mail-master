package com.ys.mail.util;

import com.google.common.base.CaseFormat;

/**
 * 属性命名转换工具
 *
 * @author CRH
 * @date 2022-05-24 10:33
 * @since 1.0
 */
public class CaseUtil {

    /**
     * 转骆驼命名
     *
     * @param caseFormat 转换类型
     * @param str        原字符串，如 test_add -> testAdd
     * @return 转换结果
     */
    public static String toCamelCase(CaseFormat caseFormat, String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(caseFormat, str);
    }

    /**
     * 转骆驼命名
     *
     * @param str 原字符串
     * @return 转换结果
     */
    public static String toCamelCase(String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

}
