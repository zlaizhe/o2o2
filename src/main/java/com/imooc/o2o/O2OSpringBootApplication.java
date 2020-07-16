package com.imooc.o2o;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class O2OSpringBootApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(O2OSpringBootApplication.class, args);
    }

    //配置自定义的RedisTemplate
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //用于 Java 与 JSON 的序列化和反序列化
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);// key采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer); // hash的key也采用String的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);  // value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer); // hash的value序列化方式采用jackson
        template.afterPropertiesSet();
        return template;
    }

}
