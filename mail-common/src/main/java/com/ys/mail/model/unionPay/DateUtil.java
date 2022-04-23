package com.ys.mail.model.unionPay;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * 类名：UtilDate
 * 功能：日期工具类
 * 日期：2014-06-02
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究银盛支付接口使用，只是提供一个参考。
 *
 * @author ghdhj
 */
public class DateUtil {

    /**
     * 年月日时分秒毫秒(无下划线) yyyyMMddHHmmssSSS
     */
    public static final String DT_LONGS = "yyyyMMddHHmmssSSS";

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String DT_LONG = "yyyyMMddHHmmss";

    /**
     * 完整时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String SIMPLE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年月日   yyyy-MM-dd
     */
    public static final String DT_SHORT_ = "yyyy-MM-dd";

    /**
     * 年月日(无下划线) yyyyMMdd
     */
    public static final String DT_SHORT = "yyyyMMdd";

    /**
     * 时分秒(无下划线) HHmmss
     */
    public static final String DT_TIME = "HHmmss";

    /**
     * 获当前日期
     *
     * @param dateFormat
     * @return String
     */
    public static String getCurrentDate(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    /**
     * 获取自定义格式化日期
     *
     * @param date
     * @param dateFormat
     * @return String
     */
    public static String getDateFormat(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 获取当前日期前一天
     * 格式：YYYYMMDD
     */
    public static String getCurrentDateFront() {
        String strDate = new SimpleDateFormat("yyyyMMdd").format(addDays(new Date(), -1));
        strDate = strDate.substring(0, 4) + strDate.substring(4, 6) + strDate.substring(6);
        return strDate;
    }

    /**
     * 按日加减日期
     *
     * @param date：日期
     * @param num：要加减的日数
     * @return 成功，则返回加减后的日期；失败，则返回null
     */
    public static Date addDays(Date date, int num) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, num);

        return c.getTime();
    }

    /**
     * 按月加减日期
     *
     * @param date：日期
     * @param num：要加减的月数
     * @return 成功，则返回加减后的日期；失败，则返回null
     */
    public static Date addMonths(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, num);
        return c.getTime();
    }

    /**
     * 按年加减日期
     *
     * @param date：日期
     * @param num：要加减的年数
     * @return 成功，则返回加减后的日期；失败，则返回null
     */
    public static Date addYears(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, num);
        return c.getTime();
    }

    /**
     * 按秒 加减日期
     *
     * @param date：日期
     * @param num：要加减的秒
     * @return 成功，则返回加减后的日期；失败，则返回null
     */
    public static Date addSeconds(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, num);
        return c.getTime();
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int getRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }


    /**
     * 检查日期字符串是否合法
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return 布尔
     * 'yyyyMMdd'  'HHmmss' 所以年月日不是yyyymmdd
     */
    @SuppressWarnings("unused")
    public static boolean isValidDate(String dateStr, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        //来强调严格遵守该格式
        df.setLenient(false);
        Date date = null;
        try {
            date = df.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @return
     * @功能描述：生成msgId
     */
    public static String getMsgId() {
        int ran = getRandom(10);
        String msgId = getCurrentDate(DT_LONG) + "-" + String.valueOf(ran);
        return msgId;
    }

    /**
     * 获取当前日期的下个月1号
     */
    public static Date nextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberUtils.INTEGER_ONE);
        calendar.add(Calendar.MONTH, NumberUtils.INTEGER_ONE);
        return calendar.getTime();
    }

    public static List<Date> nextMonthFirstDates(int num) {
        List<Date> dates = new ArrayList<>();
        for (int i = NumberUtils.BYTE_ONE; i <= num; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, NumberUtils.INTEGER_ONE);
            calendar.add(Calendar.MONTH, i);
            dates.add(calendar.getTime());
        }
        return dates;
    }

   /* public static void main(String[] args) {
        Date date = addSeconds(new Date(), 10);
        try {
            Thread.sleep(7000);
            if(System.currentTimeMillis() > date.getTime()){
                System.out.println(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(false);
    }*/
}
