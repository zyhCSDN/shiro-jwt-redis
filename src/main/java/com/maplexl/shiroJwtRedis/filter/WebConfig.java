package com.maplexl.shiroJwtRedis.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器到spring容器
 * @author: Zhaoyongheng
 * @date: 2021/6/23
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    原文链接：https://blog.csdn.net/qq_36688143/article/details/79499339

    //注入时，如果提示没有某个bean，需要在此注入体上面加 @Component 注解，或者，注解手写bean注入
//         * jwt拦截器
//         * @return
//         */
//        @Bean
//        public JwtInterceptor jwtInterceptor() {
//            return new JwtInterceptor();
//        }

    @Autowired
    JwtInterceptor jwtInterceptor;
    /**
     * 添加jwt拦截器
     * @param registry
     */
    final String[] notLoginInterceptPaths = {"/signIn","/login","/index/**","/register/**","/kaptcha.jpg/**","/kaptcha/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                //拦截配置 // 将我们上步定义的实现了HandlerInterceptor接口的拦截器实例jwtInterceptor添加InterceptorRegistration中，
                // 并设置过滤规则，让所有请求都要经过jwtInterceptor拦截。
                .addPathPatterns("/**")
                //排除配置
                .excludePathPatterns(notLoginInterceptPaths);
    }


}