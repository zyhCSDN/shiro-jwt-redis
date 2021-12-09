//package com.maplexl.shiroJwtRedis.config;
//
///**
// * @author 枫叶
// * @date 2020/11/1
// */
//
//import com.maplexl.shiroJwtRedis.filter.JwtFilter;
//import com.maplexl.shiroJwtRedis.shiro.ShiroRealm;
//import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
//import org.apache.shiro.mgt.DefaultSubjectDAO;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//import javax.servlet.Filter;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * shiro 配置类
// */
//@Configuration
//public class ShiroConfig {
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        // 过滤器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        // 配置不会被拦截的链接 顺序判断
//        // 登录接口排除
//        filterChainDefinitionMap.put("/login", "anon");
//        // 注册接口排除
//        filterChainDefinitionMap.put("/register", "anon");
//        //登出接口排除
//        filterChainDefinitionMap.put("/logout", "anon");
//        //欢迎页排除
//        filterChainDefinitionMap.put("/", "anon");
//        filterChainDefinitionMap.put("/**/*.js", "anon");
//        filterChainDefinitionMap.put("/**/*.css", "anon");
//        filterChainDefinitionMap.put("/**/*.html", "anon");
//        filterChainDefinitionMap.put("/**/*.jpg", "anon");
//        filterChainDefinitionMap.put("/**/*.png", "anon");
//        filterChainDefinitionMap.put("/**/*.ico", "anon");
//        //swagger资源排除
//        filterChainDefinitionMap.put("/swagger-ui.html/**","anon");
//        filterChainDefinitionMap.put("/swagger-resources/**","anon");
//        filterChainDefinitionMap.put("/v2/**","anon");
//
//        // 添加自己的过滤器并且取名为jwt
//        Map<String, Filter> filterMap = new HashMap<>(1);
//        filterMap.put("jwt", new JwtFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);
//        // 过滤链定义，从上向下顺序执行，一般将放在最为下边
//        filterChainDefinitionMap.put("/**", "jwt");
//
//        //未授权界面返回JSON
//        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
//        shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean("securityManager")
//    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myRealm);
//
//        /*
//         * 关闭shiro自带的session，详情见文档
//         * http://shiro.apache.org/session-management.html#SessionManagement-
//         * StatelessApplications%28Sessionless%29
//         */
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        securityManager.setSubjectDAO(subjectDAO);
//
//        return securityManager;
//    }
//
//    /**
//     * 下面的代码是添加注解支持
//     */
//    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
//
//    @Bean
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//}
