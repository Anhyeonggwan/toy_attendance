package com.attendance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig {
	
	 @Value("${spring.redis.host}")
	 private String redisHost;
	 
	 @Value("${spring.redis.port}")
	 private int redisPort;
	 
	 @Bean
	 public RedisConnectionFactory redisConnectionFactory() {
	     return new LettuceConnectionFactory(redisHost, redisPort);
	 }
	 
	 @Bean
	 public RedisTemplate<String, Object> redisTemplate() {
	     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	     // 적용 내용
	     redisTemplate.setKeySerializer(new StringRedisSerializer());
	     redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
	     redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	     redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
	     // 적용 내용 끝
	     redisTemplate.setConnectionFactory(redisConnectionFactory());
	     return redisTemplate;
	 }

}
