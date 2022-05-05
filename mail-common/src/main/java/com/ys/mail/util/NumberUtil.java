package com.ys.mail.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Desc 数字工具类
 * @Author CRH
 * @Create 2022-02-15 11:35
 */
public class NumberUtil {

    /**
     * 获取一组数字中缺省的最小数字
     *
     * @param oldNumbers 一组正整数
     * @return 最小数字
     * eg:[-2,0,1,3,4,6] -> 2
     */
    public static Integer minMissNumber(Collection<Integer> oldNumbers) {
        return minMissNumber(oldNumbers, null, null);
    }

    /**
     * 在指定范围内获取一组数字中缺省的最小数字
     *
     * @param oldNumbers 一组正整数
     * @param min        最小范围，默认为0
     * @param max        最大范围，默认为数组中最大值
     * @return 最小数字
     */
    public static Integer minMissNumber(Collection<Integer> oldNumbers, Integer min, Integer max) {
        // 结果
        List<Integer> missNumberList = minMissNumberList(oldNumbers, min, max);
        if (BlankUtil.isNotEmpty(missNumberList)) {
            // 获取最小值
            return Collections.min(missNumberList);
        } else {
            // 如果没有空缺则返回旧数组中最大+1的数字
            return Collections.max(oldNumbers) + 1;
        }
    }

    /**
     * 返回范围内所有却省的数字
     *
     * @param oldNumbers 原数据
     * @return 一组却省的数字
     */
    public static List<Integer> minMissNumberList(Collection<Integer> oldNumbers) {
        return minMissNumberList(oldNumbers, null, null);
    }

    /**
     * 返回范围内所有却省的数字
     *
     * @param oldNumbers 原数据
     * @param min        最小范围，默认为0
     * @param max        最大范围，默认为数组中最大值
     * @return 一组却省的数字
     */
    public static List<Integer> minMissNumberList(Collection<Integer> oldNumbers, Integer min, Integer max) {
        if (BlankUtil.isEmpty(oldNumbers)) {
            oldNumbers = Stream.of(NumberUtils.INTEGER_ZERO).collect(Collectors.toSet());
        }
        // 新数组
        List<Integer> newNumbers = new ArrayList<>();
        // 过滤掉负数
        oldNumbers = oldNumbers.stream().filter(s -> s >= 0).collect(Collectors.toList());
        // 计算起始截止的值
        if (BlankUtil.isEmpty(min)) {
            min = NumberUtils.INTEGER_ZERO;
        }
        if (BlankUtil.isEmpty(max)) {
            max = Collections.max(oldNumbers);
        }
        // 遍历找出空缺数字
        for (int i = min; i <= max; i++) {
            if (oldNumbers.contains(i)) continue;
            newNumbers.add(i);
        }
        return newNumbers;
    }

    /**
     * 获取符合条件区间的值，在区间中进行匹配
     * - 注意：超过定义区间的范围则只返回默认值
     * - 区间：(0,100] 表示大于0 且 小于等于100，这里使用{min}与{max}表示
     *
     * @param rangeList    对象数组区间：每个对象必须包含{min、max、value}属性
     * @param number       数值，如：20
     * @param defaultValue 默认值
     * @return 最终值
     */
    public static String getScale(List<Object> rangeList, Double number, String defaultValue) {
        AtomicReference<String> value = new AtomicReference<>(defaultValue);
        boolean anyMatch = rangeList.stream().anyMatch(rule -> {
            Integer min = (Integer) ((JSONObject) rule).get("min");
            Integer max = (Integer) ((JSONObject) rule).get("max");
            if (min < number && number <= max) {
                value.set(String.valueOf(((JSONObject) rule).get("value")));
                return true;
            }
            return false;
        });
        return value.get();
    }

    /**
     * 设置保留小数点并返回有效值
     *
     * @param d 数值
     * @return 有效值 或 0
     */
    public static Object ifZeroReturnZero(Double d) {
        Object o = ifZeroReturnNull(d);
        if (BlankUtil.isNotEmpty(o)) return o;
        return NumberUtils.INTEGER_ZERO;
    }

    /**
     * 设置保留小数点并返回有效值
     *
     * @param d 数值
     * @return 有效值 或 null
     */
    public static Object ifZeroReturnNull(Double d) {
        double v = setScale(d);
        if (v != NumberUtils.DOUBLE_ZERO) return v;
        return null;
    }

    /**
     * 保留2位小数点，并四舍五入
     *
     * @param d 数值
     * @return 字符串
     */
    public static double setScale(Double d) {
        d = ifNullDefaultValue(d);
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取非空的数字
     *
     * @param d 数值对象的原值
     * @return 原值或默认值
     */
    public static double ifNullDefaultValue(Double d) {
        return BlankUtil.isEmpty(d) ? NumberUtils.DOUBLE_ZERO : d;
    }

    /**
     * 值转换，当值为空时不进行转换
     *
     * @param str 目标字符串
     * @return 转换结果
     */
    public static Long longOf(String str) {
        if (BlankUtil.isNotEmpty(str)) {
            return Long.valueOf(str);
        }
        return null;
    }

    public static Integer integerOf(String str) {
        if (BlankUtil.isNotEmpty(str)) {
            return Integer.valueOf(str);
        }
        return null;
    }

    public static Double doubleOf(String str) {
        if (BlankUtil.isNotEmpty(str)) {
            return Double.valueOf(str);
        }
        return null;
    }

    public static void main(String[] args) {
        // 模拟读取设置
        String rules = "[{\"min\":0,\"max\":3000,\"value\":\"0.001\"},{\"min\":3000,\"max\":30000,\"value\":\"0.002\"},{\"min\":30000,\"max\":50000,\"value\":\"0.003\"}]";
        JSONArray parseArray = JSONUtil.parseArray(rules);
        System.out.println("费率：" + getScale(parseArray, 1000L * 100.0, "0.005"));
        System.out.println("费率：" + getScale(parseArray, 100L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, -100L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, 3000L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, 30000L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, 50000L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, 80000L * 100.0, "0.001"));
        System.out.println("费率：" + getScale(parseArray, 80000L * 100.0, "0.005"));
        // System.out.println(minMissNumber(Arrays.asList(1, 3, 4), 1, null));
        // System.out.println(minMissNumber(Arrays.asList(1, 2, 3, 4), 1, 4));
        // System.out.println(minMissNumber(Arrays.asList(1, 2, 3, 4), 1, 5));
        //
        // System.out.println(minMissNumber(Arrays.asList(1, 2, 3, 4), 0, 5));
        // System.out.println(minMissNumber(Arrays.asList(1, 2, 3, 4), null, 5));
        // System.out.println(minMissNumber(Arrays.asList(1, 2, 3, 4), null, null));
        //
        // System.out.println(minMissNumberList(Arrays.asList(1, 4), 1, null));
        // System.out.println(minMissNumberList(Arrays.asList(1, 4, 5), 1, 5));
        // System.out.println(minMissNumberList(Arrays.asList(1, 4, 5), 0, 6));
        //
        // System.out.println(minMissNumberList(Arrays.asList(), 0, 1));
        // System.out.println(minMissNumberList(Arrays.asList(0), 0, 1));
        // System.out.println(minMissNumberList(Arrays.asList(1), 0, 1));
        // System.out.println(minMissNumberList(Arrays.asList(0, 1), 0, 1));
    }

}
