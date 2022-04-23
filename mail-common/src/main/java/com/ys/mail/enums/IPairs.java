package com.ys.mail.enums;

/**
 * @Desc 通用枚举接口
 * - 用于限定枚举边界，以及更方便使用工具类
 * - 实现该接口的枚举，无需额外提供GETTER方法，只需实现以下2个接口即可
 * @Author CRH
 * @Create 2022-02-28 11:27
 */
public interface IPairs<K, V, C extends Enum<C>> {

    /**
     * 默认实现：返回枚举对象
     *
     * @return obj
     */
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
