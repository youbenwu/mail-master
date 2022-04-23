package com.ys.mail.util;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JavaForDateUtil {
    /**
     * 获取当前时间范围结束时间
     */
    public static String JavaForDate(int type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        if (type == 1) {
            //当天
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 0);
            Date date = calendar.getTime();
            String formatDay = dateFormat.format(date);
            return formatDay;
            //      System.out.println(formatDay);//20211122
        } else if (type == 2) {
            //过去3月
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -3);
            Date month = calendar.getTime();
            String formatMonth = dateFormat.format(month);
            return formatMonth;
            //      System.out.println(formatMonth);//20210825
        } else if (type == 3) {
            //过去6月
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -6);
            Date monthSix = calendar.getTime();
            String formatMonthSix = dateFormat.format(monthSix);
            return formatMonthSix;
            //      System.out.println(formatMonthSix);//20210525
        } else if (type == 4) {
            //昨天
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);
            Date date = calendar.getTime();
            return dateFormat.format(date);
            //      System.out.println(formatMonthSix);//20210525
        } else if (type == 5) {
            // 获取当月第一天
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
            return dateFormat.format(calendar.getTime());
        } else if (type == 6) {
            // 获取当月最后一天
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } else {
            //过去3年
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, -3);
            Date year = calendar.getTime();
            String formatYear = dateFormat.format(year);
            //      System.out.println(formatYear);//20181125
            return formatYear;
        }
    }

    public static void main(String[] args) {
        // 测试日期 2022-01-06，得到以下结果
        System.out.println(JavaForDate(1)); // 20220106
        System.out.println(JavaForDate(2)); // 20211006
        System.out.println(JavaForDate(3)); // 20210706
        System.out.println(JavaForDate(5)); // 20220101
        System.out.println(JavaForDate(6)); // 20220131

        // 先解析出date对象，默认格式为2022-01-01 00:00:00 然后直接toString即可
        Date date = DateUtil.parse(JavaForDate(5));
        System.out.println(date.toString());
        // 转换字符串显示格式
        DateUtil.format(date,"yyyy/MM/dd HH:mm");

    }
}