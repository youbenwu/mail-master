package com.ys.mail.annotation;


import com.ys.mail.validator.BlankOrPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;


/**
 * @Desc 自定义注解：允许为空的正则表达式，主要用于前端传参
 * @Author CRH
 * @Create 2021-12-27 15:47
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BlankOrPatternValidator.class})
public @interface BlankOrPattern {

    String regexp();

    Pattern.Flag[] flags() default {};

    String message() default "{javax.validation.constraints.Pattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Pattern[] value();
    }

    public static enum Flag {
        UNIX_LINES(1),
        CASE_INSENSITIVE(2),
        COMMENTS(4),
        MULTILINE(8),
        DOTALL(32),
        UNICODE_CASE(64),
        CANON_EQ(128);

        private final int value;

        private Flag(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

}
