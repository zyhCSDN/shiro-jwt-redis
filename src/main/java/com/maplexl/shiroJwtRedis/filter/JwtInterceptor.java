package com.maplexl.shiroJwtRedis.filter;

import com.maplexl.shiroJwtRedis.constant.CommonConstant;
import com.maplexl.shiroJwtRedis.pojo.User;
import com.maplexl.shiroJwtRedis.services.JwtToken;
import com.maplexl.shiroJwtRedis.services.UserService;
import com.maplexl.shiroJwtRedis.util.JwtUtil;
import com.maplexl.shiroJwtRedis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截器拦截带有注解的接口 JwtInterceptor.java
 *
 * @author: Zhaoyongheng
 * @date: 2021/6/23
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        log.info("token================={}", token);
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(JwtToken.class)) {
            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if (jwtToken.required()) {
                if (token == null) {
                    throw new RuntimeException("无token令牌，请重新登录");
                }
                log.info("————————身份认证开始——————————:  ");
                    if(!JwtUtil.refreshToken(token)){
                        throw new RuntimeException("Token失效请重新登录!");
                    }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
