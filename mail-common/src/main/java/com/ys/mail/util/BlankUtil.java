package com.ys.mail.util;

/**
 * Created by cent on 2018/3/1.
 */

import java.util.Collection;
import java.util.Map;

/**
 * 对象空判断工具类
 *
 * @author cent
 * @date 2021.5
 */
public enum BlankUtil {
    ;

    /**
     * 集合判空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() <= 0;
    }

    /**
     * 字符串判空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }

    /**
     * Map判空
     *
     * @param t
     * @return
     */
    public static <T extends Map> boolean isEmpty(T t) {
        return t == null || t.keySet().size() <= 0;
    }

    /**
     * 对象判空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj instanceof Collection) {
            return isEmpty((Collection) obj);
        }

        return false;
    }

    /**
     * 数组判空
     *
     * @param objs
     * @return
     */
    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length <= 0;
    }

    /**
     * 不定参判空（只要有一个为空即返回true）
     *
     * @param objs
     * @return
     */
    public static boolean isExistsEmpty(Object... objs) {
        for (Object obj : objs) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合非空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * 字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Map非空
     *
     * @param t
     * @return
     */
    public static <T extends Map> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    /**
     * 对象非空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 数组非空
     *
     * @param objs
     * @return
     */
    public static boolean isNotEmpty(Object[] objs) {
        return !isEmpty(objs);
    }
}
