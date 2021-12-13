package com.maplexl.shiroJwtRedis.filter;

import com.maplexl.shiroJwtRedis.constant.CommonConstant;
import com.maplexl.shiroJwtRedis.pojo.User;
import com.maplexl.shiroJwtRedis.services.JwtToken;
import com.maplexl.shiroJwtRedis.services.UserService;
import com.maplexl.shiroJwtRedis.util.JwtUtil;
import com.maplexl.shiroJwtRedis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 拦截器拦截带有注解的接口 JwtInterceptor.java
 *
 * @author: Zhaoyongheng
 * @date: 2021/6/23
 */

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;


    //注解方式拦截
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
//        // 从 http 请求头中取出 token
//        String token = httpServletRequest.getHeader("token");
//        log.info("token================={}", token);
//        // 如果不是映射到方法直接通过
//        if (!(object instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) object;
//        Method method = handlerMethod.getMethod();
//        //检查有没有需要用户权限的注解
//        if (method.isAnnotationPresent(JwtToken.class)) {
//            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
//            if (jwtToken.required()) {
//                if (token == null) {
//                    throw new RuntimeException("无token令牌，请重新登录");
//                }
//                log.info("————————身份认证开始——————————:  ");
//                    if(!JwtUtil.refreshToken(token)){
//                        throw new RuntimeException("Token失效请重新登录!");
//                    }
//            }
//        }
//        return true;
//    }

    //url方式拦截
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws IOException {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        String basePath = httpServletRequest.getContextPath();
        String path = httpServletRequest.getRequestURI();
        log.error("basePath:{}", basePath);
        log.error("path:{}", path);
        //是否进行登陆拦截 此步骤可以注释掉，使用 webConfig里的配置信息  排除配置
//        if (!doLoginInterceptor(path, basePath)) {
//            return true;
//        }
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        log.info("————————身份认证开始——————————:  ");
        log.info("token:{}", token);
        if (token == null) {
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.setHeader("token", token);
            httpServletResponse.getWriter().print("无token令牌，请重新登录");
            return false;
        }
        if (!JwtUtil.refreshToken(token)) {
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.setHeader("token", token);
            httpServletResponse.getWriter().print("Token失效请重新登录!");
            return false;
        }
        return true;
    }

    /**
     * 是否进行登陆过滤
     *
     * @param path
     * @param basePath
     * @return
     */
    private boolean doLoginInterceptor(String path, String basePath) {
        path = path.substring(basePath.length());
        Set<String> notLoginPaths = new HashSet<>();
        //设置不进行登录拦截的路径：登录注册和验证码
        //notLoginPaths.add("/");
        notLoginPaths.add("/index");
        notLoginPaths.add("/signIn");
        notLoginPaths.add("/login");
        notLoginPaths.add("/register");
        notLoginPaths.add("/kaptcha.jpg");
        notLoginPaths.add("/kaptcha");
        //notLoginPaths.add("/sys/logout");
        //notLoginPaths.add("/loginTimeout");

        if (notLoginPaths.contains(path)) {
            return false;
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
