package com.ys.mail.plugins;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiImplicitParam;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.WebMvcRequestHandler;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.base.Strings.emptyToNull;
import static springfox.documentation.schema.Types.isBaseType;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;
import static springfox.documentation.swagger.readers.parameter.Examples.examples;
import static springfox.documentation.swagger.schema.ApiModelProperties.allowableValueFromString;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-09 14:16
 * @since 1.0
 */
public class EnumOperationImplicitParameterReader implements OperationBuilderPlugin {
    private final DescriptionResolver descriptions;

    public EnumOperationImplicitParameterReader(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(readParameters(context));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    static Parameter implicitParameter(DescriptionResolver descriptions, ApiImplicitParam param, OperationContext context) {
        ModelRef modelRef = maybeGetModelRef(param);
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        String name = param.name();
        String value = param.value();
        parameterBuilder.name(name)
                        .description(descriptions.resolve(value))
                        .defaultValue(param.defaultValue())
                        .required(param.required())
                        .allowMultiple(param.allowMultiple())
                        .modelRef(modelRef)
                        .allowableValues(allowableValueFromString(param.allowableValues()))
                        .parameterType(emptyToNull(param.paramType()))
                        .parameterAccess(param.access())
                        .order(SWAGGER_PLUGIN_ORDER)
                        .scalarExample(param.example())
                        .complexExamples(examples(param.examples()));

        // 自定义文档处理
        enumDocumentHandler(context, parameterBuilder, name, value);

        return parameterBuilder.build();
    }

    /**
     * 枚举文档处理器 - 直接作用于controller层接口中的参数，对于参数对象里面的属性无效
     *
     * @param context          参数上下文
     * @param parameterBuilder 参数构建器
     * @param name             注解@ApiImplicitParam中的名称
     * @param value            注解@ApiImplicitParam中的值
     */
    private static void enumDocumentHandler(OperationContext context, ParameterBuilder parameterBuilder, String name, String value) {
        try {
            Field requestContext = context.getClass().getDeclaredField("requestContext");
            requestContext.setAccessible(true);
            RequestMappingContext requestMappingContext = (RequestMappingContext) requestContext.get(context);
            Field handler = requestMappingContext.getClass().getDeclaredField("handler");
            handler.setAccessible(true);
            WebMvcRequestHandler webMvcRequestHandler = (WebMvcRequestHandler) handler.get(requestMappingContext);
            List<ResolvedMethodParameter> parameters = webMvcRequestHandler.getParameters();
            for (ResolvedMethodParameter parameter : parameters) {
                Optional<String> optional = parameter.defaultName();
                if (!optional.isPresent()) {
                    continue;
                }
                String defaultName = optional.get();
                List<Annotation> annotations = parameter.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (BlankUtil.isNotEmpty(name) && BlankUtil.isNotEmpty(defaultName) && name.equals(defaultName)) {
                        if (annotation instanceof EnumDocumentValid) {
                            EnumDocumentValid enumDocument = (EnumDocumentValid) annotation;
                            // 统一处理
                            EnumDocumentUtil.handlerDocument(enumDocument, parameterBuilder, name, value);
                        }
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private static ModelRef maybeGetModelRef(ApiImplicitParam param) {
        String dataType = MoreObjects.firstNonNull(emptyToNull(param.dataType()), "string");
        AllowableValues allowableValues = null;
        if (isBaseType(dataType)) {
            allowableValues = allowableValueFromString(param.allowableValues());
        }
        if (param.allowMultiple()) {
            return new ModelRef("", new ModelRef(dataType, allowableValues));
        }
        return new ModelRef(dataType, allowableValues);
    }

    private List<Parameter> readParameters(OperationContext context) {
        Optional<ApiImplicitParam> annotation = context.findAnnotation(ApiImplicitParam.class);
        List<Parameter> parameters = Lists.newArrayList();
        if (annotation.isPresent()) {
            parameters.add(implicitParameter(descriptions, annotation.get(), context));
        }
        return parameters;
    }
}
