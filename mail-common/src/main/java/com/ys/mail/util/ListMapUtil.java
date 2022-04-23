package com.ys.mail.util;

import java.util.List;
import java.util.Map;

/**
 * @Desc List/Map工具类
 * @Author CRH
 * @Create 2022-04-01 17:38
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
        if (BlankUtil.isNotEmpty(vMap)) return vMap.get(id);
        else return null;
    }
}
