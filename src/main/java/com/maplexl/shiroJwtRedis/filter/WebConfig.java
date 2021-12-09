package com.maplexl.shiroJwtRedis.filter;

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

    /**
     * 添加jwt拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                //拦截配置 // 将我们上步定义的实现了HandlerInterceptor接口的拦截器实例authenticationInterceptor添加InterceptorRegistration中，并设置过滤规则，所有请求都要经过authenticationInterceptor拦截。
                .addPathPatterns("/**");
//                 .addPathPatterns("/twjd/**");
//                .excludePathPatterns("/twjd/login","/twjd/error");
                //排除配置
//                .excludePathPatterns("/login");
    }

    /**
     * jwt拦截器
     * @return
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}