package com.maplexl.shiroJwtRedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //是否开启swagger，根据环境来选择
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket api() {
        //全局参数
        Parameter token = new ParameterBuilder().name("token")
                .description("用户登陆令牌")
                .parameterType("header")
                .modelRef(new ModelRef("String"))
                //是否必须
                .required(false)
                .build();
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .groupName("api")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.maplexl.shiroJwtRedis.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("jwt-shiro-redis")
                .description("api")
                // 作者信息
                .contact(new Contact("枫叶", "https://www.cnblogs.com/junlinsky/", "203051919@qq.com"))
                .version("1.0.0")
                .build();
    }
}

