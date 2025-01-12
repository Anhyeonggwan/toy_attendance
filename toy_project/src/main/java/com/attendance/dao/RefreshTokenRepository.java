package com.attendance.dao;

import java.util.Objects;
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
        redisTemplate.expire(refreshToken.getRefreshToken(), 24L, TimeUnit.HOURS);
    }
	
	public void blackSave(final String accessToken) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, "blacklist");
	}
	
	public boolean findBlackList(final String accessToken) {
		boolean result = false;
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String blackList = valueOperations.get(accessToken);
		
		if(Objects.isNull(blackList)) result = true;
		
		return result;
	}
	
	public void deleteRefreshToken(final String refreshToken) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		redisTemplate.delete(refreshToken);
	}
	
	public RefreshToken findByIdx(final String refreshToken) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        int memberIdx = valueOperations.get(refreshToken);
        
        if (Objects.isNull(memberIdx)) {
            return null;
        }

        return new RefreshToken(refreshToken, memberIdx);
    }

}
