package com.ys.mail.config;


import com.ys.mail.domain.SwaggerProperties;
import com.ys.mail.plugins.EnumApiModelPropertyPropertyPlugin;
import com.ys.mail.plugins.EnumExpandedParameterPlugin;
import com.ys.mail.plugins.EnumOperationImplicitParameterReader;
import com.ys.mail.plugins.EnumOperationImplicitParametersReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger基础配置
 * @author 070
 */
@Slf4j
@Configuration
public abstract class BaseSwaggerConfig {

    @Autowired
    private DescriptionResolver descriptions;
    @Autowired
    private EnumTypeDeterminer enumTypeDeterminer;

    @Bean
    public Docket createRestApi() {
        SwaggerProperties swaggerProperties = swaggerProperties();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getApiBasePackage()))
                .paths(PathSelectors.any())
                .build();
        if (swaggerProperties.isEnableSecurity()) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        return docket;
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    private List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/*/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }

    /**
     * 自定义Swagger配置
     * @return  无返回值
     */
    public abstract SwaggerProperties swaggerProperties();

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

    @Bean
    @ConditionalOnProperty(value = "swagger.plugin.enum-support", matchIfMissing = true)
    public EnumApiModelPropertyPropertyPlugin enumApiModelPropertyPropertyPlugin() {
        log.info("loading custom swagger enum plugin -> EnumApiModelPropertyPropertyPlugin");
        return new EnumApiModelPropertyPropertyPlugin(descriptions);
    }
}
