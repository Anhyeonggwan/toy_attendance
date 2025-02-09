package com.attendance.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.attendance.dao.MemberDao;
import com.attendance.dao.RefreshTokenRepository;
import com.attendance.util.ApiException;
import com.attendance.vo.Member;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final JwtProvider jwtProvider;
	private final MemberDao memberDao;
	private final RefreshTokenRepository tokenRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
		String token = "";
		String accessToken = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
		logger.info("access token >>> " + accessToken);
		
		token = "Bearer " + accessToken;
		
		String userName = "";
		
		// Bearer token 검증 후 user name 조회
		if(token != null && !token.isEmpty()) {
			String jwtToken = token.substring(7);
			userName = jwtProvider.getUsernameFromToken(jwtToken);
		}
		
		if(userName != null && !userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
			String uri = request.getRequestURI();
			if(tokenRepository.findBlackList(token.substring(7))) {
				if(uri.contains("logout")) {
					tokenRepository.blackSave(token.substring(7));
				}
			}else {
				throw new ApiException("401", "접근 권한이 없습니다.");
			}
			
			// Spring Security Context Holder 인증 정보 set
			SecurityContextHolder.getContext().setAuthentication(getUserAuth(userName));
		}
		
		filterChain.doFilter(request, response);
	}
	
	/**
     * token의 사용자 idx를 이용하여 사용자 정보 조회하고, UsernamePasswordAuthenticationToken 생성
     * 
     * @param username 사용자 idx
     * @return 사용자 UsernamePasswordAuthenticationToken
     */
	private UsernamePasswordAuthenticationToken getUserAuth(String username) {
		Member member = memberDao.findUserByIdx(username);
		return new UsernamePasswordAuthenticationToken(member.getUserId(), member.getUserPassword()
				, Collections.singleton(new SimpleGrantedAuthority(member.getUserGrade()))); // 멤버 등급에 ROLE_ 접두사가 붙어야함 ex) ROLE_NORMAL
	}

}
