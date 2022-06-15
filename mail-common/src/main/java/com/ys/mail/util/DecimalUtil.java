package com.ys.mail.util;

import com.ys.mail.constant.StringConstant;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 对象空判断工具类
 *
 * @author DT
 * @date 2021.12.20
 */
public class DecimalUtil {

    /**
     * 加法计算（result = x + y）
     *
     * @param x 被加数（可为null）
     * @param y 加数 （可为null）
     * @return 和 （可为null）
     * @author dengcs
     */
    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        if (x == null) {
            return y;
        }
        if (y == null) {
            return x;
        }
        return x.add(y);
    }

    /**
     * 加法计算（result = a + b + c + d）
     *
     * @param a 被加数（可为null）
     * @param b 加数（可为null）
     * @param c 加数（可为null）
     * @param d 加数（可为null）
     * @return BigDecimal （可为null）
     * @author dengcs
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d) {
        BigDecimal ab = add(a, b);
        BigDecimal cd = add(c, d);
        return add(ab, cd);
    }

    /**
     * 累加计算(result=x + result)
     *
     * @param x      被加数（可为null）
     * @param result 和 （可为null,若被加数不为为null，result默认值为0）
     * @return result 和 （可为null）
     * @author dengcs
     */
    public static BigDecimal accumulate(BigDecimal x, BigDecimal result) {
        if (x == null) {
            return result;
        }
        if (result == null) {
            result = new BigDecimal("0");
        }
        return result.add(x);
    }

    /**
     * 减法计算(result = x - y)
     *
     * @param x 被减数（可为null）
     * @param y 减数（可为null）
     * @return BigDecimal 差 （可为null）
     * @author dengcs
     */
    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return null;
        }
        return x.subtract(y);
    }

    /**
     * 乘法计算(result = x × y)
     *
     * @param x 乘数(可为null)
     * @param y 乘数(可为null)
     * @return BigDecimal 积
     * @author dengcs
     */
    public static BigDecimal multiply(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return null;
        }
        return x.multiply(y);
    }

    /**
     * 除法计算(result = x ÷ y)
     *
     * @param x 被除数（可为null）
     * @param y 除数（可为null）
     * @return 商 （可为null,四舍五入，默认保留20位小数）
     * @author dengcs
     */
    public static BigDecimal divide(BigDecimal x, BigDecimal y) {
        if (x == null || y == null || y.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        // 结果为0.000..时，不用科学计数法展示
        return stripTrailingZeros(x.divide(y, 20, RoundingMode.HALF_DOWN));
    }

    /**
     * 转为字符串(防止返回可续计数法表达式)
     *
     * @param x 要转字符串的小数
     * @return String
     * @author dengcs
     */
    public static String toPlainString(BigDecimal x) {
        if (x == null) {
            return null;
        }
        return x.toPlainString();
    }

    /**
     * 去除多余的零
     *
     * @param x 原字符串
     * @return 转换结果
     */
    public static String toPlainString(String x) {
        if (BlankUtil.isEmpty(x)) {
            return StringConstant.BLANK;
        }
        return new BigDecimal(x).stripTrailingZeros().toPlainString();
    }

    /**
     * 保留小数位数
     *
     * @param x     目标小数
     * @param scale 要保留小数位数
     * @return BigDecimal 结果四舍五入
     * @author dengcs
     */
    public static BigDecimal scale(BigDecimal x, int scale) {
        if (x == null) {
            return null;
        }
        return x.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 整型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(Integer x) {
        if (x == null) {
            return null;
        }
        return new BigDecimal(x.toString());
    }

    /**
     * 长整型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(Long x) {
        if (x == null) {
            return null;
        }
        return new BigDecimal(x.toString());
    }

    /**
     * 双精度型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(Double x) {
        if (x == null) {
            return null;
        }
        return new BigDecimal(x.toString());
    }

    /**
     * 单精度型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(Float x) {
        if (x == null) {
            return null;
        }
        return new BigDecimal(x.toString());
    }

    /**
     * 字符串型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(String x) {
        if (x == null) {
            return null;
        }
        return new BigDecimal(x);
    }

    /**
     * 对象类型转为BigDecimal
     *
     * @param x(可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal toBigDecimal(Object x) {
        if (x == null) {
            return null;
        }
        BigDecimal result = null;
        try {
            result = new BigDecimal(x.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 数值内容转long
     *
     * @param number 数值
     * @return long
     */
    public static Long objToLong(Object number) {
        if (number == null) {
            return null;
        }
        return toBigDecimal(number).longValue();
    }

    /**
     * 数值内容转long
     *
     * @param number 数值
     * @return long
     */
    public static Integer objToInt(Object number) {
        if (number == null) {
            return null;
        }
        return toBigDecimal(number).intValue();
    }

    /**
     * 倍数计算，用于单位换算
     *
     * @param x        目标数(可为null)
     * @param multiple 倍数 (可为null)
     * @return BigDecimal (可为null)
     * @author dengcs
     */
    public static BigDecimal multiple(BigDecimal x, Integer multiple) {
        if (x == null || multiple == null) {
            return null;
        }
        return DecimalUtil.multiply(x, toBigDecimal(multiple));
    }

    /**
     * 去除小数点后的0（如: 输入1.000返回1）
     *
     * @param x 目标数(可为null)
     * @return
     */
    public static BigDecimal stripTrailingZeros(BigDecimal x) {
        if (x == null) {
            return null;
        }
        return x.stripTrailingZeros();
    }

    /**
     * 长整型数字 除以 100 后 转换成字符串
     *
     * @param longNumber 如：1500
     * @return 返回：15
     */
    public static String longToStrForDivider(Long longNumber) {
        if (BlankUtil.isEmpty(longNumber)) return null;
        BigDecimal divide = new BigDecimal(longNumber).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return divide.stripTrailingZeros().toPlainString();
    }

    public static Double longToDoubleForDivider(Long longNumber) {
        if (BlankUtil.isEmpty(longNumber)) {
            return NumberUtils.DOUBLE_ZERO;
        }
        BigDecimal divide = new BigDecimal(longNumber).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return divide.doubleValue();
    }

    public static String strToStrForDivider(String strNumber) {
        if (BlankUtil.isEmpty(strNumber)) return null;
        BigDecimal divide = new BigDecimal(strNumber).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return divide.stripTrailingZeros().toPlainString();
    }

    /**
     * 字符串数字 乘以 100 后转换成数字
     *
     * @param strNumber 字符串数字，只对两位小数点生效，多余将被舍去
     * @return 返回长整型数字
     */
    public static Long strToLongForMultiply(String strNumber) {
        if (BlankUtil.isEmpty(strNumber)) return null;
        BigDecimal multiply = new BigDecimal(strNumber).multiply(new BigDecimal("100"));
        return multiply.longValue();
    }

    public static Long strToLongForMultiply(Double strNumber) {
        return strToLongForMultiply(String.valueOf(strNumber));
    }

    /**
     * @param one BigDecimal值
     * @param two BigDecimal值
     * @return int 返回结果
     * one.compareTo(two) == -1 ==> one小于two
     * one.compareTo(two) == 0 ==> one等于two
     * one.compareTo(two) == 1 ==> one大于two
     * one.compareTo(two) > -1 ==> one大于等于two
     * one.compareTo(two) < 1 ==> one小于等于two
     * 功能描述: 比较两个数大小
     * @author: 邱天保 <qiutianbao>
     * @since: 2022/3/8 16:26
     */
    public static int compareToBigDecimal(BigDecimal one, BigDecimal two) {
        if (Objects.isNull(one) || Objects.isNull(two)) {
            throw new RuntimeException("传入的值不能为空");
        }
        return one.compareTo(two);
    }

//    public static void main(String[] args) {
//        System.out.println(DecimalUtil.longToStrForDivider(25));
//        System.out.println(DecimalUtil.longToStrForDivider(176359));
//        System.out.println(DecimalUtil.longToStrForDivider(1));
//        System.out.println(DecimalUtil.longToStrForDivider(0));
//        System.out.println(DecimalUtil.longToStrForDivider(-13));
//
//        System.out.println(DecimalUtil.strToLongForMultiply("38.1"));
//        System.out.println(DecimalUtil.strToLongForMultiply("381"));
//        System.out.println(DecimalUtil.strToLongForMultiply("3"));
//        System.out.println(DecimalUtil.strToLongForMultiply("38.154"));// 多余将被舍去
//        System.out.println(DecimalUtil.strToLongForMultiply("38.15"));
//    }

    public static void main(String[] args) {
        // 定义两个变量，进行运算
        BigDecimal a = new BigDecimal("100");
        BigDecimal b = new BigDecimal("3.14");

        // 加 a+b
        BigDecimal c1 = DecimalUtil.add(a, b);
        // 减 a-b
        BigDecimal c2 = DecimalUtil.subtract(a, b);
        // 乘 a*b
        BigDecimal c3 = DecimalUtil.multiply(a, b);
        // 除 a/b
        BigDecimal c4 = DecimalUtil.subtract(a, b);

        // 累加 sum = a + sum
        BigDecimal sum = null;
        sum = DecimalUtil.accumulate(a, sum);

        // 将变量a结果保留2位小数
        BigDecimal c5 = DecimalUtil.scale(a, 2);

        // 变量a的1000倍运算   a*1000
        BigDecimal c6 = DecimalUtil.multiple(a, 1000);

        //Integer、Long、Float、Double、String、Object转为BigDecimal。
        Object obj = 123;
        BigDecimal c7 = DecimalUtil.toBigDecimal(obj);

        // BigDecimal转为字符串
        String str = DecimalUtil.toPlainString(a);

        // 不同数据类型之间混合运算（如：Double类型除以Long类型）
        BigDecimal mixCalc = DecimalUtil.divide(DecimalUtil.toBigDecimal(1.23D), DecimalUtil.toBigDecimal(1234567L));


    }
}
