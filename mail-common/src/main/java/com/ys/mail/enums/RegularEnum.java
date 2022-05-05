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
    ID("^\\d{19}$"),
    /**
     * 新格式ID：至少一位数字即可，移除19位限制，防止后续ID长度变化，适用更多场景
     */
    KEY("^\\d+$"),
    /**
     * 纬度，有效的范围从-90度到90度
     */
    LAT("^[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$"),
    /**
     * 经度，有效的范围从-180度到180度
     */
    LNG("^[\\-\\+]?(0(\\.\\d{1,10})?|([1-9](\\d)?)(\\.\\d{1,10})?|1[0-7]\\d{1}(\\.\\d{1,10})?|180\\.0{1,10})$");

    private final String reg;
}
