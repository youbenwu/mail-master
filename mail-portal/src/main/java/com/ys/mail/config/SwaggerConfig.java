package com.ys.mail.config;


import com.ys.mail.domain.SwaggerProperties;
import com.ys.mail.plugins.EnumExpandedParameterPlugin;
import com.ys.mail.plugins.EnumOperationImplicitParameterReader;
import com.ys.mail.plugins.EnumOperationImplicitParametersReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 *
 * @author 070
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private DescriptionResolver descriptions;
    @Autowired
    private EnumTypeDeterminer enumTypeDeterminer;

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                                .apiBasePackage("com.ys.mail.controller")
                                .title(globalConfig.getProjectName())
                                .description(globalConfig.getProjectName() + "APP相关接口文档")
                                .contactName("ys")
                                .version("1.0")
                                .enableSecurity(true)
                                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "swagger.plugin.enum-support", matchIfMissing = true)
    public EnumExpandedParameterPlugin enumExpandedParameterPlugin() {
        log.info("loading custom swagger enum plugin -> EnumExpandedParameterPlugin");
        return new EnumExpandedParameterPlugin(descriptions, enumTypeDeterminer);
    }

    @Bean
    @ConditionalOnProperty(value = "swagger.plugin.enum-support", matchIfMissing = true)
    public EnumOperationImplicitParameterReader enumOperationImplicitParameterReader() {
        log.info("loading custom swagger enum plugin -> EnumOperationImplicitParameterReader");
        return new EnumOperationImplicitParameterReader(descriptions);
    }

    @Bean
    @ConditionalOnProperty(value = "swagger.plugin.enum-support", matchIfMissing = true)
    public EnumOperationImplicitParametersReader enumOperationImplicitParametersReader() {
        log.info("loading custom swagger enum plugin -> EnumOperationImplicitParametersReader");
        return new EnumOperationImplicitParametersReader(descriptions);
    }
}
