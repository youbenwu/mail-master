package com.ys.mail.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-07 18:57
 * @since 1.0
 */
public class ReflectTool {

    /**
     * 开放静态常量权限
     *
     * @param field 需要打开的字段
     * @throws NoSuchFieldException   异常
     * @throws IllegalAccessException 异常
     */
    public static void openStaticFinal(Field field) throws NoSuchFieldException, IllegalAccessException {
        // 如果field为private,则需要使用该方法使其可被访问
        field.setAccessible(true);
        // 修改权限
        Field modifierField = Field.class.getDeclaredField("modifiers");
        modifierField.setAccessible(true);
        // 把指定的field中的final修饰符去掉
        modifierField.setInt(field, field.getModifiers() & Modifier.FINAL);
    }

    public static void setStaticFinal(Field field, Object oldValue, Object newValue) throws Exception {
        // 打开权限
        openStaticFinal(field);

        // 为指定field设置新值
        field.set(oldValue, newValue);
    }

    public static void setPrivate(Field field, Object newValue) throws Exception {
        // 如果field为private,则需要使用该方法使其可被访问
        field.setAccessible(true);
        // 为指定field设置新值
        field.set(null, newValue);
    }

    public static void setAnnotation() {

    }
}
