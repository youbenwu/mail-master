package com.ys.mail.annotation;

import com.ys.mail.constant.StringConstant;
import com.ys.mail.util.BlankUtil;
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
import java.util.stream.Collectors;

/**
 * 枚举文档校验注解（二合一）
 *
 * @author CRH
 * @date 2022-06-08 15:38
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EnumDocumentValid.EnumDocumentValidator.class)
public @interface EnumDocumentValid {

    /**
     * 错误提示
     */
    String message() default "类型错误/不匹配，请检查";

    /**
     * 必须的属性，用于分组校验
     */
    Class<?>[] groups() default {};

    /**
     * 负载值
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类，必传（用于校验、反射枚举类）
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 包含列表：当该属性有值时，只匹配该列表 <br/><br/>
     * 枚举的子集，且该列表同时作用于文档生成和枚举校验
     */
    int[] include() default {};

    /**
     * 排除类型：当该属性有值且 {include} 属性长度为0时生效，表示被排除的类型无法再进行匹配 <br/><br/>
     * 枚举的子集，且该列表同时作用于文档生成和枚举校验
     */
    int[] exclude() default {};

    /**
     * 是否开启校验，true->校验（默认），false->不校验
     */
    boolean isValid() default true;

    /**
     * 是否允许为空（无值是跳过，有值校验），true->必须传值（默认），false->允许为空
     */
    boolean required() default true;

    /**
     * 是否生成文档，true->自动生成（默认），false->不生成
     */
    boolean isDocument() default true;

    /**
     * 覆盖文档值，该值将会覆盖 { @ApiModelProperty / @ApiImplicitParam } 中的value属性，可选
     */
    String coverValue() default "";

    /**
     * 连接符号(主要用于连接key与value，生成文档使用)，默认为 ->
     */
    String jointMark() default StringConstant.SIMPLE_ARROWS;

    /**
     * 分割符号(主要用于区别每个枚举项，生成文档使用)，默认为 ，
     */
    String delimiter() default StringConstant.ZH_COMMA;

    /**
     * 内部校验器
     */
    class EnumDocumentValidator implements ConstraintValidator<EnumDocumentValid, Object> {

        private final static Logger LOGGER = LoggerFactory.getLogger(EnumContains.EnumContainsValidator.class);

        private Class<? extends Enum<?>> enumClass;
        private int[] include;
        private int[] exclude;
        private boolean required;
        private boolean isValid;

        @Override
        public void initialize(EnumDocumentValid annotation) {
            this.enumClass = annotation.enumClass();
            this.include = annotation.include();
            this.exclude = annotation.exclude();
            this.required = annotation.required();
            this.isValid = annotation.isValid();
        }

        @Override
        public boolean isValid(Object key, ConstraintValidatorContext context) {
            // 关闭校验会直接跳过
            if (!isValid) {
                return true;
            }

            // 非空检测
            if (!required && BlankUtil.isEmpty(key)) {
                return true;
            }

            Integer value = Integer.valueOf(String.valueOf(key));

            // 包含、排除模式
            if (null != include && include.length > 0) {
                List<Integer> tempList = Arrays.stream(include).boxed().collect(Collectors.toList());
                boolean contains = tempList.contains(value);
                if (!contains) {
                    return false;
                }
            } else if (null != exclude && exclude.length > 0) {
                for (Integer s : exclude) {
                    if (StringUtil.compareKey(s, value)) {
                        return false;
                    }
                }
            }

            // 实际类型匹配
            boolean contains = EnumTool.contains(enumClass, "", key);
            LOGGER.info("EnumDocumentValidator：Enum Type Valid...【EnumClass：{}】-【key：{}，isPass：{}】", enumClass.getName(), key, contains);
            return contains;
        }
    }

}
