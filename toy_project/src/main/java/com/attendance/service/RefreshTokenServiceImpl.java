package com.attendance.service;

import org.springframework.stereotype.Service;

import com.attendance.config.JwtProvider;
import com.attendance.config.RefreshToken;
import com.attendance.util.ApiException;
import com.attendance.vo.JwtToken;

import lombok.RequiredArgsConstructor;

/**
 * Refresh Token으로 Token 갱신 요청 시 동작하는 Service이다. 
 * 만약 Refresh Token이 검증되지 않으면, Custom Exception을 발생시킨다. 
 * 만약 Refresh Token이 검증되면, 기존에 가지고 있는 사용자의 Refresh Token을 제거하고 새로운 Refresh Token을 생성, 저장한다. 
 * 그리고 저장한 Refresh Token을 새로운 Access Token과 함께 반환한다.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{
	
	private final JwtProvider jwtProvider;
	
	/**
     * refresh token을 이용하여 access token, refresh token 재발급
     *
     * @param refreshToken refresh token
     * @return RefreshTokenResponseDTO
     */
	@Override
	public JwtToken refreshToken(final String refreshToken) {
		
		checkRefreshToken(refreshToken);
		
		var id = RefreshToken.getRefreshToken(refreshToken);
		
		String newAccessToken = jwtProvider.generateAccessToken(id);
		
		RefreshToken.removeUserRemoveRefreshToken(id);
		
		String newRefreshToken = jwtProvider.generateRefreshToken(id);
		RefreshToken.putRefreshToken(newRefreshToken, id);
		
		return JwtToken.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
	}
	
	/**
     * refresh token 검증
     *
     * @param refreshToken refresh token
     */
	private void checkRefreshToken(final String refreshToken) {
		if(Boolean.FALSE.equals(jwtProvider.validateToken(refreshToken))) {
			throw new ApiException("1001", "토큰이 존재하지 않습니다.");
		}
	}
}