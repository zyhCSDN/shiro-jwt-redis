package com.maplexl.shiroJwtRedis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maplexl.shiroJwtRedis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@Configuration
public class RedisConfig {

    /**
     *
     *  序列化方式，参考如下
     *
     * https://www.cnblogs.com/yaowen/p/9107443.html
     */


    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //设置序列化策略 序列化为String
        RedisSerializer stringSerializer = new StringRedisSerializer();
        //序列化为Json
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //将类名称序列化到json串中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //设置RedisUtil工具类
        RedisUtil.setRedisTemplate(redisTemplate);
        return redisTemplate;
    }
}
