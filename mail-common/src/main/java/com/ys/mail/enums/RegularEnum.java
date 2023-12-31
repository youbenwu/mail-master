package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 正则枚举，主要统一管理一些常用正则
 *
 * @author CRH
 * @date 2022-04-23 15:43
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum RegularEnum {

    /**
     * 空正则，默认
     */
    BLANK(""),
    /**
     * 主键ID，19位，只适用于长度固定的ID
     */
    @Deprecated
    ID("^\\d{19}$"),
    /**
     * 主键KEY：至少一位数字即可，移除位数限制，防止后续ID长度变化，适用更多场景
     */
    KEY("^\\d+$"),
    /**
     * 纬度，有效的范围从-90度到90度
     */
    LAT("^[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$"),
    /**
     * 经度，有效的范围从-180度到180度
     */
    LNG("^[\\-\\+]?(0(\\.\\d{1,10})?|([1-9](\\d)?)(\\.\\d{1,10})?|1[0-7]\\d{1}(\\.\\d{1,10})?|180\\.0{1,10})$"),
    /**
     * 文件名，格式：xxx.xxx / xxx001-xxx.xxx等
     */
    FILENAME("^[^\\u4E00-\\u9FA5]+\\.[a-zA-Z\\d]+$"),
    ZERO_ONE("[01]"),

    ;

    private final String reg;
}
