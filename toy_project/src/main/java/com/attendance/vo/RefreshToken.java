package com.attendance.vo;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@RedisHash(value = "jwtToken", timeToLive = 60*60*24)
public class RefreshToken { // redis에 저장할 객체를 정의한다.
	
	@Id
    private String refreshToken;
	private int idx;
    
}
