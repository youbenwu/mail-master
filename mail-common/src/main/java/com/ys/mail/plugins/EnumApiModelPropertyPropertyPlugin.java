package com.ys.mail.plugins;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-14 14:17
 * @since 1.0
 */
public class EnumApiModelPropertyPropertyPlugin implements ModelPropertyBuilderPlugin {

    private final DescriptionResolver descriptions;

    @Autowired
    public EnumApiModelPropertyPropertyPlugin(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<ApiModelProperty> annotation = Optional.absent();
        Optional<EnumDocumentValid> documentValid = Optional.absent();
        String name = "";

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        Optional<BeanPropertyDefinition> beanPropertyDefinition = context.getBeanPropertyDefinition();
        if (beanPropertyDefinition.isPresent()) {
            BeanPropertyDefinition propertyDefinition = beanPropertyDefinition.get();
            annotation = annotation.or(findPropertyAnnotation(
                    propertyDefinition,
                    ApiModelProperty.class));
            documentValid = documentValid.or(findPropertyAnnotation(
                    propertyDefinition,
                    EnumDocumentValid.class));
            name = propertyDefinition.getName();
        }
        if (annotation.isPresent() && documentValid.isPresent() && BlankUtil.isNotEmpty(name)) {
            enumDocumentHandler(context, annotation.get(), documentValid.get(), name);
        }
    }

    /**
     * 枚举文档处理器 - 作用于所有Model里面带文档注解的属性(需由加上@RequestBody或作为返回值)
     *
     * @param context          上下文
     * @param apiModelProperty 文档注解
     * @param enumDocument     自定义文档生成注解
     * @param name             属性名称
     */
    private static void enumDocumentHandler(ModelPropertyContext context, ApiModelProperty apiModelProperty,
                                            EnumDocumentValid enumDocument, String name) {
        ModelPropertyBuilder builder = context.getBuilder();
        // 统一处理
        EnumDocumentUtil.handlerDocument(enumDocument, builder, name, apiModelProperty.value());
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
