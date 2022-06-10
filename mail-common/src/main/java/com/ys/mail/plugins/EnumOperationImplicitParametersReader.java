package com.ys.mail.plugins;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;

import java.util.List;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-09 16:50
 * @since 1.0
 */
public class EnumOperationImplicitParametersReader implements OperationBuilderPlugin {
    private final DescriptionResolver descriptions;

    @Autowired
    public EnumOperationImplicitParametersReader(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(readParameters(context));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }

    private List<Parameter> readParameters(OperationContext context) {
        Optional<ApiImplicitParams> annotation = context.findAnnotation(ApiImplicitParams.class);

        List<Parameter> parameters = Lists.newArrayList();
        if (annotation.isPresent()) {
            for (ApiImplicitParam param : annotation.get().value()) {
                parameters.add(EnumOperationImplicitParameterReader.implicitParameter(descriptions, param, context));
            }
        }

        return parameters;
    }
}
