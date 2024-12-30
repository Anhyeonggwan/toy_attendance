package com.attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.service.RefreshTokenService;
import com.attendance.vo.JwtToken;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
	
	private final RefreshTokenService tokenService;
	
	@PostMapping("/refresh-token")
	public ResponseEntity<JwtToken> refreshToken(@RequestParam(name = "refreshToken") String refreshToken){
		
		JwtToken jwtToken = tokenService.refreshToken(refreshToken);
		
		return ResponseEntity.ok(jwtToken);
	}

}
