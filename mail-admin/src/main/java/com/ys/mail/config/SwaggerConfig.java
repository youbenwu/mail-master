package com.ys.mail.config;

import com.ys.mail.domain.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 *
 * @author 070
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                                .apiBasePackage("com.ys.mail.controller")
                                .title(globalConfig.getProjectName() + "后台管理系统")
                                .description(globalConfig.getProjectName() + "后台相关接口文档")
                                .contactName("ys")
                                .version("1.0")
                                .enableSecurity(true)
                                .build();
    }
}
