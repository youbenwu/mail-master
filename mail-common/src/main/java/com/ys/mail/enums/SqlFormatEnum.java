package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Desc SQL中的常用格式
 * @Author CRH
 * @Create 2022-02-25 14:51
 */
@Getter
@AllArgsConstructor
public enum SqlFormatEnum {

    /**
     * 年月日
     */
    STRING_DATE_FORMAT_YMD_EQ("DATE_FORMAT({},'%Y-%m-%d') = {0}"),
    STRING_DATE_FORMAT_YMD2_EQ("DATE_FORMAT({},'%Y%m%d') = {0}"),
    /**
     * 年月
     */
    STRING_DATE_FORMAT_YM_EQ("DATE_FORMAT({},'%Y-%m') = {0}"),
    STRING_DATE_FORMAT_YM2_EQ("DATE_FORMAT({},'%Y%m') = {0}"),
    /**
     * 年
     */
    STRING_DATE_FORMAT_Y_EQ("DATE_FORMAT({},'%Y') = {0}"),

    /**
     * 在指定日期时间之间，年-月-日 时:分:秒
     */
    STRING_DATE_FORMAT_BETWEEN("DATE_FORMAT({},'%Y-%m-%d %T') BETWEEN {0} and {1}"),
    STRING_DATE_FORMAT_BETWEEN_YMD("DATE_FORMAT({},'%Y-%m-%d') BETWEEN {0} and {1}"),
    STRING_DATE_FORMAT_BETWEEN_YMD2("DATE_FORMAT({},'%Y%m%d') BETWEEN {0} and {1}"),

    STRING_DATE_FORMAT_BETWEEN_YM("DATE_FORMAT({},'%Y-%m') BETWEEN {0} and {1}"),
    STRING_DATE_FORMAT_BETWEEN_YM2("DATE_FORMAT({},'%Y%m') BETWEEN {0} and {1}"),

    STRING_DATE_FORMAT_BETWEEN_Y("DATE_FORMAT({},'%Y') BETWEEN {0} and {1}"),
    /**
     * 小于指定日期时间
     */
    STRING_DATE_FORMAT_LT("DATE_FORMAT({},'%Y-%m-%d %T') < {0}"),
    /**
     * 小于指定日期时间
     */
    STRING_DATE_FORMAT_LE("DATE_FORMAT({},'%Y-%m-%d %T') <= {0}"),
    /**
     * 小于等于指定日期时间
     */
    STRING_DATE_FORMAT_EQ("DATE_FORMAT({},'%Y-%m-%d %T') = {0}"),
    /**
     * 大于等于指定日期时间
     */
    STRING_DATE_FORMAT_GE("DATE_FORMAT({},'%Y-%m-%d %T') >= {0}"),
    /**
     * 大于指定日期时间
     */
    STRING_DATE_FORMAT_GT("DATE_FORMAT({},'%Y-%m-%d %T') > {0}"),
    /**
     * 不等于指定日期时间
     */
    STRING_DATE_FORMAT_NE("DATE_FORMAT({},'%Y-%m-%d %T') != {0}"),
    /**
     * 在指定日期之间
     */
    STRING_DATE_BETWEEN("DATE({}) BETWEEN {0} and {1}"),
    /**
     * 小于指定日期，年-月-日
     */
    STRING_DATE_LT("DATE({}) < {0}"),
    /**
     * 小于指定日期
     */
    STRING_DATE_LE("DATE({}) <= {0}"),
    /**
     * 小于等于指定日期
     */
    STRING_DATE_EQ("DATE({}) = {0}"),
    /**
     * 大于等于指定日期
     */
    STRING_DATE_GE("DATE({}) >= {0}"),
    /**
     * 大于指定日期
     */
    STRING_DATE_GT("DATE({}) > {0}"),
    /**
     * 不等于指定日期
     */
    STRING_DATE_NE("DATE({}) != {0}"),
    /**
     * 在指定时间之间，时:分:秒
     */
    STRING_TIME_BETWEEN("TIME({}) BETWEEN {0} and {1}"),
    /**
     * 小于指定时间
     */
    STRING_TIME_LT("TIME({}) < {0}"),
    /**
     * 小于指定时间
     */
    STRING_TIME_LE("TIME({}) <= {0}"),
    /**
     * 小于等于指定时间
     */
    STRING_TIME_EQ("TIME({}) = {0}"),
    /**
     * 大于等于指定时间
     */
    STRING_TIME_GE("TIME({}) >= {0}"),
    /**
     * 大于指定时间
     */
    STRING_TIME_GT("TIME({}) > {0}"),
    /**
     * 不等于指定时间
     */
    STRING_TIME_NE("TIME({}) != {0}"),

    /**
     * 统计SQL
     */
    STRING_SUM("IFNULL(SUM({}),0) {}");

    final String content;
}
