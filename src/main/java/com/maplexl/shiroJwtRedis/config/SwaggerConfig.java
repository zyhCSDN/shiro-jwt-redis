package com.maplexl.shiroJwtRedis.config;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
    // 定义分隔符
    private static final String splitor = ";";
    @Bean
    public Docket api() {
        System.out.println("Swagger===========================================");
        //全局参数
//        Parameter token = new ParameterBuilder().name("token")
//                .description("用户登陆令牌")
//                .parameterType("header")
//                .modelRef(new ModelRef("String"))
//                //是否必须
//                .required(true)
//                .build();
//        ArrayList<Parameter> parameters = new ArrayList<>();
//        parameters.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
//                .globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
//                .groupName("api")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.maplexl.shiroJwtRedis.controller"))
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                . securitySchemes( securitySchemes())
                .securityContexts(securityContexts());
    }



    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList<>();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("token", authorizationScopes));
        return securityReferences;
    }


//http://项目实际地址/swagger-ui.html
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("jwt-shiro-redis")
                .description("SHIRO Api文档")
                // 作者信息
                .contact(new Contact("ZHAOYONGHENG", "https://www.baidu.com", "1461254354@qq.com"))
                .version("1.0")
                .build();
    }
}

