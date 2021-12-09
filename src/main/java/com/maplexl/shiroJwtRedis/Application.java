package com.maplexl.shiroJwtRedis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 枫叶
 * @date 2020/10/31
 */
@SpringBootApplication
@MapperScan("com.maplexl.shiroJwtRedis.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
