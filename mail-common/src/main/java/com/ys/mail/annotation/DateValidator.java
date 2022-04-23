package com.ys.mail.annotation;

import com.ys.mail.util.BlankUtil;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ys.mail.annotation.DateValidator.DFT.YMD;

/**
 * @Desc 通用日期校验注解，可用于参数或实体类上
 * @Author CRH
 * @Create 2022-03-02 20:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DateValidator.DateValidatorHandler.class)
public @interface DateValidator {

    /**
     * 消息提示
     */
    String message() default "日期为空/格式不正确";

    /**
     * 可选参数，用于分组校验
     */
    Class<?>[] groups() default {};

    /**
     * 可选参数，负载值
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 常用日期格式，枚举类，默认选项：yyyy-MM-dd
     */
    DFT dft() default YMD;

    /**
     * 当 {DFT.FORMAT} 时生效
     */
    String format() default "";

    /**
     * 当 {DFT.PATTERN} 时生效
     * 使用自定义正则表达式校验
     */
    String pattern() default "";

    /**
     * 是否允许为空，true->必须传值，false->允许为空
     */
    boolean required() default false;

    /**
     * 常用日期格式
     */
    @NoArgsConstructor
    enum DFT {
        Y,// yyyy
        M,// MM
        D,// dd
        T,// HH:mm:ss
        YM,// yyyy-MM
        YMD,// yyyy-MM-dd
        YMDT,// yyyy-MM-dd HH:mm:ss
        FORMAT,// 自定义格式类型，如：yyyy/MM/dd等
        PATTERN // 使用正则匹配，如：[0-2]\d{3}-(0[1-9]|1[0-2]) 等
    }

    class DateValidatorHandler implements ConstraintValidator<DateValidator, String> {

        private DFT dft;
        private String format;
        private String pattern;
        private boolean required;

        @Override
        public void initialize(DateValidator dateValidator) {
            this.dft = dateValidator.dft();
            this.format = dateValidator.format();
            this.pattern = dateValidator.pattern();
            this.required = dateValidator.required();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            // 非空检测
            if (required && BlankUtil.isEmpty(value)) return false;
            if (BlankUtil.isEmpty(value)) return true;
            // 校验模式
            switch (dft) {
                case FORMAT:
                    // 自定义格式
                    if (BlankUtil.isEmpty(format)) return false;
                    return isLegal(value, format);
                case PATTERN:
                    // 自定义正则
                    if (BlankUtil.isEmpty(pattern)) return false;
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(value);
                    return m.matches();
                default:
                    // 使用常用匹配
                    return defaultHandle(dft, value);
            }
        }

        /**
         * 默认处理
         *
         * @param dft  日期格式类型
         * @param date 日期字符串
         * @return 日期是否合法
         */
        private boolean defaultHandle(DFT dft, String date) {
            switch (dft) {
                case Y:
                    return isLegal(date, "yyyy");
                case M:
                    return isLegal(date, "MM");
                case D:
                    return isLegal(date, "dd");
                case T:
                    return isLegal(date, "HH:mm:ss");
                case YM:
                    return isLegal(date, "yyyy-MM");
                case YMDT:
                    return isLegal(date, "yyyy-MM-dd HH:mm:ss");
                case YMD:
                default:
                    return isLegal(date, "yyyy-MM-dd");
            }
        }

        /**
         * 判断日期是否合法
         *
         * @param date   日期
         * @param format 格式
         * @return 是否合法
         */
        private boolean isLegal(String date, String format) {
            // 长度判断
            if (date.length() != format.length()) return false;
            // 解析判断
            try {
                SimpleDateFormat sd = new SimpleDateFormat(format);
                sd.setLenient(false);
                sd.parse(date);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
