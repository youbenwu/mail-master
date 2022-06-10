package com.ys.mail.util;

import cn.hutool.core.util.ReflectUtil;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.constant.WarningsConstant;
import com.ys.mail.enums.IPairs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 枚举工具类（依赖 HuTool 工具）
 * - 配合接口，更方便使用 {@link IPairs}
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public class EnumTool {

    /**
     * 获取枚举项拼接列表
     *
     * @param iPairsClass 枚举类型
     * @param include     包含列表
     * @param exclude     排除列表
     * @param jointMark   连接符
     * @param delimiter   分割符
     * @return 拼接列表
     */
    public static <E extends Enum<E>> String values(Class<? extends IPairs<?, ?, ?>> iPairsClass,
                                                    List<Integer> include, List<Integer> exclude,
                                                    CharSequence jointMark, CharSequence delimiter) {
        // 获取枚举数组
        IPairs<?, ?, ?>[] enumConstants = iPairsClass.getEnumConstants();

        // 遍历拼接结果，支持包含排除模式：优先包含列表
        return Arrays.stream(enumConstants).map(e -> {
            Integer key = (Integer) e.key();
            String value = String.valueOf(e.value());

            if (BlankUtil.isNotEmpty(include)) {
                if (include.contains(key)) {
                    return String.format("%s%s%s", key, jointMark, value);
                }
            } else if (BlankUtil.isNotEmpty(exclude)) {
                if (!exclude.contains(key)) {
                    return String.format("%s%s%s", key, jointMark, value);
                }
            } else {
                return String.format("%s%s%s", key, jointMark, value);
            }
            return StringConstant.BLANK;
        }).filter(BlankUtil::isNotEmpty).collect(Collectors.joining(delimiter));
    }

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
    @SuppressWarnings(WarningsConstant.UNCHECKED)
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
    @SuppressWarnings(WarningsConstant.UNCHECKED)
    public static <K, V, C extends Enum<C>> V getValue(Class<? extends IPairs<K, V, C>> iPairsClass, K key) {
        IPairs<K, V, C> iPairs = (IPairs<K, V, C>) getEnum(iPairsClass, key);
        return Optional.ofNullable(iPairs).map(IPairs::value).orElse(null);
    }

    /**
     * 生成 JAVA静态常量 枚举文档 <br/>
     * 注意：一般为首次或当枚举类型变更时调用，通过测试类执行即可
     *
     * @param iPairsClass 枚举类，必须实现{@link IPairs}接口才可以使用该方法
     * @param docName     文档名称：如 用户类型、订单状态等
     * @return 生成的Java静态常量代码，拷贝到枚举类中即可
     */
    public static <K, V, C extends Enum<C>> String documentCode(Class<? extends IPairs<K, V, C>> iPairsClass, String docName) {
        String document = document(iPairsClass, docName);
        String sb = "public static final String DOCUMENT" +
                StringConstant.EQUAL_SIGN +
                StringConstant.DOUBLE_QUOTE +
                StringConstant.PLACEHOLDER_S +
                StringConstant.DOUBLE_QUOTE +
                StringConstant.EN_SEMICOLON;
        return String.format(sb, document);
    }

    /**
     * 生成枚举文档，常用于API接口中使用 <br/>
     * 注意：一般为首次或当枚举类型变更时调用，通过测试类执行即可
     *
     * @param iPairsClass 枚举类，必须实现{@link IPairs}接口才可以使用该方法
     * @param docName     文档名称：如 用户类型、订单状态等
     * @return 生成枚举中所有属性拼接后的文档
     */
    public static <K, V, C extends Enum<C>> String document(Class<? extends IPairs<K, V, C>> iPairsClass, String docName) {
        IPairs<K, V, C>[] enumConstants = iPairsClass.getEnumConstants();
        StringBuilder sb = new StringBuilder();

        if (BlankUtil.isNotEmpty(docName)) {
            sb.append(docName)
              .append(StringConstant.ZH_COLON);
        }

        Arrays.stream(enumConstants).forEach(e -> {
            sb.append(e.key()).append(StringConstant.SIMPLE_ARROWS).append(e.value()).append(StringConstant.ZH_COMMA);
        });
        return sb.substring(0, sb.length() - 1);
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
    @SuppressWarnings(WarningsConstant.UNCHECKED)
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
            if (null != e) {
                break;
            }
        }
        return e;
    }

}
