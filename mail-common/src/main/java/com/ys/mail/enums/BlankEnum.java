package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 空枚举
 *
 * @author CRH
 * @date 2022-06-01 17:51
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum BlankEnum implements IPairs<Integer, String, BlankEnum> {

    ;
    final Integer key;
    final String value;
}
