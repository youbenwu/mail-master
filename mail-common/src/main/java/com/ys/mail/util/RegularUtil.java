package com.ys.mail.util;

import com.ys.mail.constant.FigureConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * 自定义正则表达式接口-DT
 *
 * @author ghdhj
 */
public class RegularUtil {

    /**
     * 验证19位纯数字正则表达式方法
     */
    public static boolean nineMatches(String regex) {
        final String patten = "^\\d{19}$";
        return regex.matches(patten);
    }

    /**
     * 日期组合正则：支持 年、年月、年月日，如2022、202203、20220301
     */
    public static final String DATE_REGEX = "^$|^[12][0-9]{3}($|(0[1-9]|1[012])$|(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$)";

    /**
     * 校验日期，格式: yyyyMMdd/yyyyMM/yyyy
     *
     * @param dateStr 日期字符串
     * @return bool
     */
    public static boolean dateMatches(String dateStr) {
        return Pattern.matches(DATE_REGEX, dateStr);
    }


    public static String desensitizePhone(String phone) {
        if (!StringUtils.isEmpty(phone)) {
            if (phone.length() == FigureConstant.ELEVEN) {
                phone = phone.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
            }
        }
        return phone;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        System.out.println(calendar.getTime());
    }

}
