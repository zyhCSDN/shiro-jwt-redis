package com.maplexl.shiroJwtRedis.config;

import com.maplexl.shiroJwtRedis.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 自动设置TokenUtil工具类参数
 *
 * @author 枫叶
 * @date 2020/11/1
 */
@Configuration
public class JwtUtilConfig {
    @Value("${jwt.EXPIRE_TIME}")
    private Long expireTime;
    @Value("${jwt.REFRESH_EXPIRE_TIME}")
    private Long refreshExpireTime;
    @Value("${jwt.TOKEN_SECRET}")
    private String tokenSecret;

    @PostConstruct
    public void init() {
        JwtUtil.setProperties(expireTime * 1000 * 60, refreshExpireTime * 60, tokenSecret);
    }
}
