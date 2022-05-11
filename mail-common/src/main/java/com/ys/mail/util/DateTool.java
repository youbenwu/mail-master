package com.ys.mail.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-30 15:31
 */
public class DateTool {

    /**
     * 检查时间是否符合要求
     *
     * @param time   目标时间
     * @param offset 偏移天数
     * @return 结果，true->符合要求，false->不符合
     */
    public static boolean checkExpireTime(Date time, Integer offset) {
        if (BlankUtil.isEmpty(time)) {
            return false;
        }
        DateTime currentDay = getNow();
        DateTime offsetDay = DateUtil.offsetDay(currentDay, offset);
        DateTime parseTime = DateUtil.parse(DateUtil.format(time, "yyyy-MM-dd"));
        return parseTime.after(offsetDay);
    }

    /**
     * 是否已经过期
     *
     * @param expireTime 过期、截止时间
     * @return 是否已过期，true->已过期，false->没过期
     */
    public static boolean isExpireTime(Date expireTime) {
        DateTime currentDay = getNow();
        return currentDay.after(expireTime);
    }

    /**
     * 计算第二天凌晨与当前时间的时间差秒数
     *
     * @return 时间戳
     */
    public static Long getNowToNextDaySeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 判断日期是否是当天
     *
     * @param time 时间
     * @return bool
     */
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    public static boolean isToday(Date date) {
        if (BlankUtil.isEmpty(date)) {
            return false;
        }
        long time = date.getTime();
        return isToday(time);
    }

    /**
     * 获取当天日期字符串，不带时间
     *
     * @return 日期字符串
     */
    public static String getTodayNow() {
        return DateUtil.formatDate(new Date());
    }

    /**
     * 获取当前日期，不带时间
     *
     * @return 结果
     */
    public static DateTime getNow() {
        String todayNow = DateTool.getTodayNow();
        return DateUtil.parseDate(todayNow);
    }

    /**
     * 获取指定日期前一天的日期字符串
     *
     * @param date 指定日期，如：2022-03-02
     * @return 前一天的日期
     */
    public static String getYesterday(String date) {
        DateTime tempDate = DateUtil.parse(date);
        DateTime yesterday = DateUtil.offsetDay(tempDate, -1);
        return DateUtil.formatDate(yesterday);
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);
        String now = sdf.format(new Date());
        return param.equals(now);
    }

    public static String getYearMonth() {
        return DateUtil.format(new Date(), "yyyy-MM");
    }

    /**
     * 获取当前日期时间字符串，格式为：yyyyMMddHHmmss
     *
     * @return 日期字符串
     */
    public static String getCurrentDateTime() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 目标时间是否在t1和t2范围之间(不包括)
     *
     * @param targetTime    目标时间
     * @param leftTimeText  t1
     * @param rightTimeText t2
     * @return 符合返回true，否则返回false
     */
    public static boolean localTimeIsBetween(LocalTime targetTime, CharSequence leftTimeText, CharSequence rightTimeText) {
        LocalTime leftTime = LocalTime.parse(leftTimeText);
        LocalTime rightTime = LocalTime.parse(rightTimeText);
        return targetTime.isAfter(leftTime) && targetTime.isBefore(rightTime);
    }

    public static boolean localTimeIsBetween(CharSequence leftTimeText, CharSequence rightTimeText) {
        return localTimeIsBetween(LocalTime.now(), leftTimeText, rightTimeText);
    }

    /**
     * 目标时间是否在t1之后(不包括)
     *
     * @param targetTime   目标时间
     * @param leftTimeText t1
     * @return 符合返回true，否则返回false
     */
    public static boolean localTimeIsAfter(LocalTime targetTime, CharSequence leftTimeText) {
        LocalTime leftTime = LocalTime.parse(leftTimeText);
        return targetTime.isAfter(leftTime);
    }

    public static boolean localTimeIsAfter(CharSequence leftTimeText) {
        return localTimeIsAfter(LocalTime.now(), leftTimeText);
    }

    /**
     * 目标时间是否在t1之前(不包括)
     *
     * @param targetTime    目标时间
     * @param rightTimeText t1
     * @return 符合返回true，否则返回false
     */
    public static boolean localTimeIsBefore(LocalTime targetTime, CharSequence rightTimeText) {
        LocalTime leftTime = LocalTime.parse(rightTimeText);
        return targetTime.isBefore(leftTime);
    }

    public static boolean localTimeIsBefore(CharSequence rightTimeText) {
        return localTimeIsBefore(LocalTime.now(), rightTimeText);
    }

    public static boolean isBefore(DateTime t1, DateTime t2) {
        return t1.isBefore(t2);
    }


    public static void main(String[] args) {
//        System.out.println(localTimeIsBetween("14:17:00", "14:40:05"));
//        System.out.println(localTimeIsAfter("14:32:20"));
//        System.out.println(localTimeIsBefore("14:32:20"));

        //DateTime dateTime = DateUtil.offsetDay(new Date(), -7);
        String format = new SimpleDateFormat(com.ys.mail.model.unionPay.DateUtil.DT_SHORT_).format(DateUtil.offsetDay(new Date(), -7));
        System.out.println(format);
    }

}
