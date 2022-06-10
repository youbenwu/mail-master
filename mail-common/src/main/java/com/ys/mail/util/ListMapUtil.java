package com.ys.mail.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * List/Map工具类
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public class ListMapUtil {

    /**
     * 根据ID获取MapList中的值
     *
     * @param listMap allList
     * @param id      ID
     * @return 找到的值
     */
    public static <V> V getListMapValue(List<Map<Long, V>> listMap, Long id) {
        Map<Long, V> vMap = listMap.stream().filter(map -> BlankUtil.isNotEmpty(map.get(id))).findFirst()
                                   .orElse(null);
        if (BlankUtil.isNotEmpty(vMap)) {
            return vMap.get(id);
        }
        return null;
    }

    /**
     * 将int数组转为List<Integer>
     *
     * @param array 数组
     * @return 结果
     */
    public static List<Integer> valueOf(int[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }
}
