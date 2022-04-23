package com.ys.mail.annotation;

import com.ys.mail.util.EnumTool;
import com.ys.mail.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Desc 自动校验枚举类型
 * @Author CRH
 * @Create 2022-03-01 12:23
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumContains.EnumContainsValidator.class)
public @interface EnumContains {

    /**
     * 错误提示
     */
    String message() default "Enum Type Error";

    /**
     * 必须的属性
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    /**
     * 负载值
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类，必传
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 仅包含：当该属性有值时，优先匹配，表示只有在以下的枚举类型才能被匹配
     * 枚举的子集
     */
    String[] include() default {};

    /**
     * 排除类型：当该属性有值且 {include} 属性长度为0时生效，表示被排除的类型无法再进行匹配
     * 枚举的子集
     */
    String[] exclude() default {};

    /**
     * 内部校验器
     */
    class EnumContainsValidator implements ConstraintValidator<EnumContains, Object> {

        private final static Logger LOGGER = LoggerFactory.getLogger(EnumContainsValidator.class);

        private Class<? extends Enum<?>> enumClass;
        private String[] include;
        private String[] exclude;

        @Override
        public void initialize(EnumContains enumContains) {
            this.enumClass = enumContains.enumClass();
            this.include = enumContains.include();
            this.exclude = enumContains.exclude();
        }

        @Override
        public boolean isValid(Object key, ConstraintValidatorContext constraintValidatorContext) {
            // 先判断是否被排除，当已经排除了则无法再进行匹配
            if (include.length == 0) {
                for (String s : exclude) if (StringUtil.compareKey(s, key)) return false;
            }
            // 仅包含判断
            if (include.length > 0) {
                List<String> tempList = Arrays.asList(include);
                boolean contains = tempList.contains(Objects.toString(key));
                if (!contains) return false;
            }
            // 实际类型匹配
            boolean contains = EnumTool.contains(enumClass, "", key);
            LOGGER.info("EnumContainsValidator：Enum Type Valid...【EnumClass：{}】-【key：{}，isPass：{}】", enumClass.getName(), key, contains);
            return contains;
        }
    }
}
