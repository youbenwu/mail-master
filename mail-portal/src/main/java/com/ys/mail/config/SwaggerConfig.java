package com.ys.mail.config;



import com.ys.mail.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * @author 070
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.ys.mail.controller")
                .title("大尾狐")
                .description("大尾狐APP相关接口文档")
                .contactName("ys")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
