package com.catchyou.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置类
 */
@EnableCaching
@SpringBootConfiguration
public class RedisConfig {
    @Bean
    public Jackson2JsonRedisSerializer<Object> jjrs() {
        Jackson2JsonRedisSerializer<Object> jjrs = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
        om.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        jjrs.setObjectMapper(om);
        return jjrs;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                                                       Jackson2JsonRedisSerializer<Object> jjrs) {
        RedisTemplate<String, Object> rt = new RedisTemplate<>();
        RedisSerializer<String> rs = new StringRedisSerializer();
        rt.setConnectionFactory(factory);
        rt.setKeySerializer(rs);
        rt.setValueSerializer(jjrs);
        rt.setHashValueSerializer(jjrs);
        return rt;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory,
                                     Jackson2JsonRedisSerializer<Object> jjrs) {
        RedisSerializer<String> rs = new StringRedisSerializer();
        RedisCacheConfiguration rcc = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(rs))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jjrs))
                .disableCachingNullValues();
        return RedisCacheManager.builder(factory).cacheDefaults(rcc).build();
    }
}