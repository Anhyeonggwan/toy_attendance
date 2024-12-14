package com.attendance.service;

import com.attendance.vo.JwtToken;

public interface RefreshTokenService {
	// refresh token을 이용하여 access token, refresh token 재발급
	public JwtToken refreshToken(final String refreshToken);

}
