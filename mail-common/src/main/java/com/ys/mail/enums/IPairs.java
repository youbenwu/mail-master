package com.ys.mail.enums;

import com.ys.mail.constant.WarningsConstant;

/**
 * 通用枚举接口
 * - 用于限定枚举边界，以及更方便使用工具类
 * - 实现该接口的枚举，无需额外提供GETTER方法，只需实现以下2个接口即可
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public interface IPairs<K, V, C extends Enum<C>> {

    /**
     * 默认实现：返回枚举对象
     *
     * @return obj
     */
    @SuppressWarnings(WarningsConstant.UNCHECKED)
    default C get() {
        return (C) this;
    }

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
