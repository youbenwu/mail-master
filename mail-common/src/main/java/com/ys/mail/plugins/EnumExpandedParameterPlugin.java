package com.ys.mail.plugins;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.constant.WarningsConstant;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterMetadataAccessor;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.collect.Lists.transform;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;
import static springfox.documentation.swagger.readers.parameter.Examples.examples;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-07 17:27
 * @since 1.0
 */
public class EnumExpandedParameterPlugin implements ExpandedParameterBuilderPlugin {

    private final DescriptionResolver descriptions;
    private final EnumTypeDeterminer enumTypeDeterminer;

    @Autowired
    public EnumExpandedParameterPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
        this.descriptions = descriptions;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }

    @Override
    public void apply(ParameterExpansionContext context) {
        Optional<ApiModelProperty> apiModelPropertyOptional = context.findAnnotation(ApiModelProperty.class);
        if (apiModelPropertyOptional.isPresent()) {
            fromApiModelProperty(context, apiModelPropertyOptional.get());
        }
        Optional<ApiParam> apiParamOptional = context.findAnnotation(ApiParam.class);
        if (apiParamOptional.isPresent()) {
            fromApiParam(context, apiParamOptional.get());
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        String allowableProperty = emptyToNull(apiParam.allowableValues());
        AllowableValues allowable = allowableValues(
                fromNullable(allowableProperty),
                context.getFieldType().getErasedType());

        maybeSetParameterName(context, apiParam.name())
                .description(descriptions.resolve(apiParam.value()))
                .defaultValue(apiParam.defaultValue())
                .required(apiParam.required())
                .allowMultiple(apiParam.allowMultiple())
                .allowableValues(allowable)
                .parameterAccess(apiParam.access())
                .hidden(apiParam.hidden())
                .scalarExample(apiParam.example())
                .complexExamples(examples(apiParam.examples()))
                .order(SWAGGER_PLUGIN_ORDER)
                .build();
    }

    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = emptyToNull(apiModelProperty.allowableValues());
        Class<?> fieldType = context.getFieldType().getErasedType();
        AllowableValues allowable = allowableValues(fromNullable(allowableProperty), fieldType);

        ParameterBuilder parameterBuilder = maybeSetParameterName(context, apiModelProperty.name());

        parameterBuilder.description(descriptions.resolve(apiModelProperty.value()))
                        .required(apiModelProperty.required())
                        .allowableValues(allowable)
                        .parameterAccess(apiModelProperty.access())
                        .hidden(apiModelProperty.hidden())
                        .scalarExample(apiModelProperty.example())
                        .order(SWAGGER_PLUGIN_ORDER).build();

        // 自定义文档处理
        enumDocumentHandler(context, parameterBuilder, apiModelProperty);

    }

    /**
     * 自定义枚举文档处理器 - 处理部分接口属性（不需要@RequestBody）
     *
     * @param context          参数上下文
     * @param parameterBuilder 参数构建器
     * @param apiModelProperty 注解
     */
    @SuppressWarnings(WarningsConstant.UNCHECKED)
    private void enumDocumentHandler(ParameterExpansionContext context, ParameterBuilder parameterBuilder, ApiModelProperty apiModelProperty) {
        try {
            Class<? extends ParameterExpansionContext> contextClass = context.getClass();
            // 获取元数据访问器
            Field metadataAccessor = contextClass.getDeclaredField("metadataAccessor");
            metadataAccessor.setAccessible(true);
            // 从上下文中获取model属性参数
            ModelAttributeParameterMetadataAccessor mapMa = (ModelAttributeParameterMetadataAccessor) metadataAccessor.get(context);
            Field annotatedElements = mapMa.getClass().getDeclaredField("annotatedElements");
            annotatedElements.setAccessible(true);
            // 获取属性列表
            List<AnnotatedElement> annotatedElementList = (List<AnnotatedElement>) annotatedElements.get(mapMa);
            for (AnnotatedElement annotatedElement : annotatedElementList) {
                // 遍历属性列表获取字段
                if (annotatedElement instanceof Field) {
                    // 获取自定义枚举文档注解(不存在则跳过)
                    EnumDocumentValid enumDocument = annotatedElement.getDeclaredAnnotation(EnumDocumentValid.class);
                    if (null == enumDocument) {
                        return;
                    }
                    // 统一处理
                    EnumDocumentUtil.handlerDocument(enumDocument, parameterBuilder, mapMa.getFieldName(), apiModelProperty.value());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ParameterBuilder maybeSetParameterName(ParameterExpansionContext context, String parameterName) {
        if (!Strings.isNullOrEmpty(parameterName)) {
            context.getParameterBuilder().name(parameterName);
        }
        return context.getParameterBuilder();
    }

    private AllowableValues allowableValues(final Optional<String> optionalAllowable, Class<?> fieldType) {

        AllowableValues allowable = null;
        if (enumTypeDeterminer.isEnum(fieldType)) {
            // allowable = ApiModelProperties.allowableValueFromString("0,1,2");
            allowable = new AllowableListValues(getEnumValues(fieldType), "LIST");
        } else if (optionalAllowable.isPresent()) {
            allowable = ApiModelProperties.allowableValueFromString(optionalAllowable.get());
        }
        return allowable;
    }

    private List<String> getEnumValues(final Class<?> subject) {
        return transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() {
            @Override
            public String apply(final Object input) {
                return input.toString();
            }
        });
    }
}
