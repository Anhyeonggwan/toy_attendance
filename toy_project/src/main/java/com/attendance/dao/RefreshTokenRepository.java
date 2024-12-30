package com.attendance.dao;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.attendance.vo.RefreshToken;

@Repository
public class RefreshTokenRepository {
	
	private RedisTemplate redisTemplate; 
	
	public RefreshTokenRepository(@Qualifier("redisTemplate") final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	public void save(final RefreshToken refreshToken) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getIdx());
        redisTemplate.expire(refreshToken.getRefreshToken(), 60L, TimeUnit.SECONDS);
    }
	
	public Optional<RefreshToken> findByIdx(final String refreshToken) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        int memberIdx = valueOperations.get(refreshToken);

        if (Objects.isNull(memberIdx)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, memberIdx));
    }

}