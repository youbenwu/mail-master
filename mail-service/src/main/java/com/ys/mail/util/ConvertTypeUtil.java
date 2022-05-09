package com.ys.mail.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONUtil;
import com.ys.mail.enums.SettingValueTypeEnum;

import java.util.Arrays;
import java.util.Objects;

import static com.ys.mail.enums.SettingValueTypeEnum.valueOf;

/**
 * @Desc 类型转换工具
 * @Author CRH
 * @Create 2022-02-15 12:18
 */
public class ConvertTypeUtil {

    /**
     * 类型转换
     *
     * @param value 需要转换的值，如 "false"
     * @param type  需要转换的类型，如 BOOLEAN
     * @return 转换后的值
     * @throws ConvertException e
     */
    public static <V> V convert(String value, String type) throws ConvertException {
        SettingValueTypeEnum choice = valueOf(type);
        switch (choice) {
            case STRING:
                return (V) Convert.convert(String.class, value);
            case INTEGER:
                return (V) Convert.convert(Integer.class, value);
            case DOUBLE:
                return (V) Convert.convert(Double.class, value);
            case BOOLEAN:
                if (BlankUtil.isEmpty(value)) {
                    return null;
                }
                value = value.toUpperCase();
                if ("TRUE".equals(value) || "FALSE".equals(value)) {
                    return (V) Convert.convert(Boolean.class, value);
                } else {
                    throw new ConvertException("类型转换异常");
                }
            case JSON:
                return (V) JSONUtil.parse(value);
            case LIST:
                return (V) Arrays.asList(value.split(","));
            default:
                return null;
        }
    }

    /**
     * 类型检查
     *
     * @param value 需要校验的值，如 0.5、false、"xxx"等
     * @param type  预匹配类型，如 STRING、BOOLEAN等
     * @return 匹配结果
     */
    public static Boolean checkType(String value, String type) {
        try {
            Object convert = convert(value, type);
            return convert != null && Objects.equals(value, Objects.toString(convert));
        } catch (Exception e) {
            return false;
        }
    }
}
