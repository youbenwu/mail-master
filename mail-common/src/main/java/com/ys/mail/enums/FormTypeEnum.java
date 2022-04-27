package com.ys.mail.enums;

/**
 * @Desc 设置表中的表达类型，可以扩展，用于添加修改时进行检查
 * @Author CRH
 * @Create 2022-02-14 15:16
 */
public enum FormTypeEnum {
    TEXT, // 文本框
    TEXTAREA,// 文本域
    BOOLEAN, // 开关类型
    NUMBER, // 整型数字
    DOUBLE, // 浮点型
    DATE, // 日期组件
    TIME, // 时间组件
    DATETIME, // 日期时间组件
    IMAGE, // 单个图像
    IMAGES, // 图像列表
    JSON // JSON字符串
}
