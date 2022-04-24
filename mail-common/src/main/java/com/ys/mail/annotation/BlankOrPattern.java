package com.ys.mail.annotation;


import com.ys.mail.enums.RegularEnum;
import com.ys.mail.util.BlankUtil;
import lombok.Data;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.engine.messageinterpolation.util.InterpolationHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;
import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * 自定义注解：允许为空的正则表达式，主要用于前端传参
 * 仅支持字符串
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BlankOrPattern.BlankOrPatternValidator.class})
public @interface BlankOrPattern {

    /**
     * 正则表达式，默认优先处理，{regexp} 与 {regEnum} 允许随意选择
     */
    String regexp() default "";

    /**
     * 正则枚举，主要为一些常用正则，当{regexp}为空时，则使用{regEnum}进行匹配
     */
    RegularEnum regEnum() default RegularEnum.BLANK;

    Pattern.Flag[] flags() default {};

    String message() default "{javax.validation.constraints.Pattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Pattern[] value();
    }

    /**
     * 注解内部处理类
     */
    @Data
    class BlankOrPatternValidator implements ConstraintValidator<BlankOrPattern, Object> {

        private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

        private java.util.regex.Pattern pattern;
        private String escapedRegexp;

        @Override
        public void initialize(BlankOrPattern parameters) {
            Pattern.Flag[] flags = parameters.flags();
            int intFlag = 0;
            for (Pattern.Flag flag : flags) {
                intFlag = intFlag | flag.getValue();
            }

            String regexp = parameters.regexp();
            RegularEnum regularEnum = parameters.regEnum();
            if (BlankUtil.isEmpty(regexp) && BlankUtil.isNotEmpty(regularEnum)) {
                regexp = regularEnum.getReg();
            }

            try {
                pattern = java.util.regex.Pattern.compile(regexp, intFlag);
            } catch (PatternSyntaxException e) {
                throw LOG.getInvalidRegularExpressionException(e);
            }

            escapedRegexp = InterpolationHelper.escapeMessageParameter(regexp);
        }

        @Override
        public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
            if (o == null) {
                return true;
            }
            String content = String.valueOf(o);
            if (content.trim().length() == 0) {
                return true;
            }
            if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                                          .addMessageParameter("regexp", escapedRegexp);
            }
            Matcher m = pattern.matcher(content);
            return m.matches();
        }
    }

}
