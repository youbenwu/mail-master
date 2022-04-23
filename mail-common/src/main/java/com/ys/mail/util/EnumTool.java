package com.ys.mail.util;

import cn.hutool.core.util.ReflectUtil;
import com.ys.mail.enums.IPairs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Desc 枚举工具类（依赖 hutool 工具）
 * - 配合接口，更方便使用 {@link IPairs}
 * @Author CRH
 * @Create 2022-02-25 18:16
 */
public class EnumTool {

    /**
     * 通用：判断枚举中是否包含该值
     * - 主要为了兼容普通的枚举类
     *
     * @param enumClass 枚举类
     * @param fieldName 字段名称；为空则按全字段匹配，从上往下优先查找
     * @param value     字段值，当fieldName为空时，匹配所有字段
     * @param <E>       枚举类
     * @return 是否包含
     */
    public static <E extends Enum<E>> boolean contains(Class<? extends Enum<?>> enumClass, String fieldName, Object value) {
        E e = of(enumClass, fieldName, value);
        return Optional.ofNullable(e).isPresent();
    }

    /**
     * 限定：判断枚举中是否包含该值
     *
     * @param iPairsClass 枚举类，必须实现{@link IPairs}接口才可以使用该方法
     * @param key         key值
     * @return 是否包含
     */
    public static <K, V, C extends Enum<C>> boolean contains(Class<? extends IPairs<K, V, C>> iPairsClass, K key) {
        IPairs<K, V, C>[] enumConstants = iPairsClass.getEnumConstants();
        long count = Arrays.stream(enumConstants).filter(e -> e.key().equals(key)).count();
        return count > 0;
    }

    /**
     * 通用：根据值获取枚举
     * - 主要为了兼容普通的枚举类
     *
     * @param enumClass 枚举类
     * @param fieldName 字段名称；为空则按全字段匹配，从上往下优先查找
     * @param value     字段值，当fieldName为空时，匹配所有字段
     * @return 返回value对应的枚举
     * - 查找不到则返回null，所以需要判空
     */
    public static <E extends Enum<E>> E getEnum(Class<? extends Enum<?>> enumClass, String fieldName, Object value) {
        return of(enumClass, fieldName, value);
    }

    /**
     * 获取枚举
     *
     * @param iPairsClass 枚举类，必须实现{@link IPairs}接口才可以使用该方法
     * @param key         key值，可以限定类型边界，不容易出错
     * @return 返回key对应的枚举
     * - 查找不到则返回null，所以需要判空
     */
    public static <K, V, C extends Enum<C>> C getEnum(Class<? extends IPairs<K, V, C>> iPairsClass, K key) {
        IPairs<K, V, C>[] enumConstants = iPairsClass.getEnumConstants();
        return (C) Arrays.stream(enumConstants).filter(e -> e.key().equals(key)).findFirst().orElse(null);
    }

    /**
     * 获取枚举值
     *
     * @param iPairsClass 枚举类，必须实现{@link IPairs}接口才可以使用该方法
     * @param key         key值
     * @return 返回key对应的枚举值
     * - 查找不到则返回null，所以需要判空
     */
    public static <K, V, C extends Enum<C>> V getValue(Class<? extends IPairs<K, V, C>> iPairsClass, K key) {
        IPairs<K, V, C> iPairs = (IPairs<K, V, C>) getEnum(iPairsClass, key);
        return Optional.ofNullable(iPairs).map(IPairs::value).orElse(null);
    }

    /**
     * 私有方法
     *
     * @param enumClass enumClass
     * @param fieldName fieldName
     * @param value     value
     * @param <E>       enum
     * @return boolean
     */
    private static <E extends Enum<E>> E of(Class<? extends Enum<?>> enumClass, String fieldName, Object value) {
        if (value instanceof CharSequence) {
            value = value.toString().trim();
        }
        final Field[] fields = ReflectUtil.getFields(enumClass);
        final Enum<?>[] enums = enumClass.getEnumConstants();
        List<Field> collect = Arrays.stream(fields)
                                    .filter(e -> !e.getType().isEnum())
                                    .filter(e -> !"$VALUES".equals(e.getName()))
                                    .filter(e -> !"ordinal".equals(e.getName()))
                                    .collect(Collectors.toList());
        if (BlankUtil.isNotEmpty(fieldName)) {
            collect = collect.stream().filter(e -> e.getName().equals(fieldName)).collect(Collectors.toList());
        }
        E e = null;
        Object finalValue = value;
        for (Field field : collect) {
            e = (E) Arrays.stream(enums)
                          .filter(en -> StringUtil.compareKey(ReflectUtil.getFieldValue(en, field), finalValue))
                          .findFirst().orElse(null);
            if (null != e) break;
        }
        return e;
    }

}
