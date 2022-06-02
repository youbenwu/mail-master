package com.ys.mail.enums;

import com.ys.mail.constant.WarningsConstant;

/**
 * 通用枚举接口 <br/>
 * - 用于限定枚举边界，以及更方便使用工具类 <br/>
 * - 实现该接口的枚举，无需额外提供GETTER方法，只需实现以下2个接口即可 <br/>
 * - 或者直接配合以下代码更加简洁 <br/>
 * - @Getter <br/>
 * - @AllArgsConstructor <br/>
 * - @Accessors(fluent = true) <br/>
 * - final Integer key; <br/>
 * - final String value; <br/>
 *
 * 实现该接口后，此枚举类所包含的操作方法有： <br/>
 * - values()  ----- 原生方法：获取枚举类的所有成员数组 <br/>
 * - valueOf() ----- 原生方法：通过枚举项名称获取枚举项 <br/>
 *
 * - name()    ----- 原生方法：获取枚举项的名称 <br/>
 * - ordinal() ----- 原生方法：获取枚举项的下标，按物理顺序在编译时已固定排序，下标从0开始 <br/>
 * - key()     ----- 扩展项：获取枚举项的key <br/>
 * - value()   ----- 扩展项：获取枚举项的value，如果只有key，则该值可由作者自定义，一般返回name <br/>
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@SuppressWarnings(WarningsConstant.UNUSED)
public interface IPairs<K, V, C extends Enum<C>> {

    /**
     * 返回枚举项的 key
     *
     * @return key
     */
    K key();

    /**
     * 返回枚举项的 value，单个值时返回key或name即可
     *
     * @return value
     */
    V value();

}
