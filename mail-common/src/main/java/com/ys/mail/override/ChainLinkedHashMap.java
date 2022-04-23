package com.ys.mail.override;

import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Desc 扩展链式Map，支持链式调用、按条件按顺序添加元素
 * @Author CRH
 * @Create 2022-04-01 10:12
 */
@NoArgsConstructor
public class ChainLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {

    public ChainLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 直接添加对象
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ChainLinkedHashMap<K, V> putObj(K key, V value) {
        return this.putObj(key, value, true);
    }

    /**
     * 按条件添加对象
     *
     * @param key       key
     * @param value     value
     * @param condition false->不添加，true->添加
     * @return this
     */
    public ChainLinkedHashMap<K, V> putObj(K key, V value, boolean condition) {
        if (condition) super.put(key, value);
        return this;
    }

}
