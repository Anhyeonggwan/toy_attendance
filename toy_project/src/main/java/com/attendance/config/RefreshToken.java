package com.attendance.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.attendance.util.ApiException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RefreshToken {	// RefreshToken을 static 하게 관리하기 위한 클래스이다.
	
	public static final Map<String, String> refreshTokens = new HashMap<>();
	
	/**
     * refresh token get
     *
     * @param refreshToken refresh token
     * @return id
     */
	public static String getRefreshToken(final String refreshToken) {
		return Optional.ofNullable(refreshTokens.get(refreshToken))
				.orElseThrow(() -> new ApiException("401", "토근이 존재하지 않습니다."));
	}
	
	/**
     * refresh token put
     *
     * @param refreshToken refresh token
     * @param id id
     */
	public static void putRefreshToken(final String refreshToken, String idx) {
		refreshTokens.put(refreshToken, idx);
	}
	
	/**
     * refresh token remove
     *
     * @param refreshToken refresh token
     */
	public static void removeRefreshToken(final String refreshToken) {
		refreshTokens.remove(refreshToken);
	}
	
	// user refresh token remove
	public static void removeUserRemoveRefreshToken(final String userId) {
		for(Map.Entry<String, String> entry : refreshTokens.entrySet()) {
			if(entry.getValue().toString().equals(userId)) {
				removeRefreshToken(entry.getKey());
			}
		}
	}
	
	public static String getRefreshTokenFromId(final String memberIdx) {
		String refreshToken = "";
		for(Map.Entry<String, String> entry : refreshTokens.entrySet()) {
			if(entry.getValue().toString().equals(memberIdx)) {
				refreshToken = entry.getKey().toString();
				break;
			}
		}
		
		return refreshToken;
	}

}
