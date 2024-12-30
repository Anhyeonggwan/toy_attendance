package com.attendance.config;

import java.io.PrintWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;

	/*
	 * 코드 설명 csrf : csrf(Cross site Request forgery) 설정을 disable 하였습니다.
	 * HttpBasic(),FormLogin() :Json을 통해 로그인을 진행하는데, 로그인 이후 refresh 토큰이 만료되기 전까지 토큰을
	 * 통한 인증을 진행할것 이기 때문에 비활성화 하였습니다. 
	 * httpBasic : Http basic Auth 기반으로 로그인 인증창이 뜬다.
	 * authorizeHttpRequests() : 인증, 인가가 필요한 URL 지정
	 * anyRequest() : requestMatchers에서 지정된 URL외의 요청에 대한 설정
	 * authenticated() : 해당 URL에 진입하기 위해서는 인증이 필요함
	 * requestMatchers("Url").permitAll() : requestMatchers에서 지정된 url은 인증, 인가 없이도 접근 허용
	 * hasRole() : 해당 URL에 진입하기 위해서 Authorization(인가, 예를 들면 ADMIN만 진입 가능)이 필요함
	 * formLogin() : Form Login 방식 적용
	 * loginPage() : 로그인 페이지 URL
	 * defaultSuccessURL() : 로그인 성공시 이동할 URL
	 * failureURL() : 로그인 실패시 이동할 URL
	 * logout() : 로그아웃에 대한 정보
	 * invalidateHttpSession() : 로그아웃 이후 전체 세션 삭제 여부
	 * sessionManagement() : 세션 생성 및 사용여부에 대한 정책 설정
	 * SessionCreationPolicy() : 정책을 설정합니다.
	 * SessionCreationPolicy.Stateless : 4가지 정책 중 하나로, 스프링 시큐리티가 생성하지 않고 존재해도 사용하지 않습니다. (JWT와 같이 세션을 사용하지 않는 경우에 사용합니다)
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf((csrfConfig) -> csrfConfig.disable())
				.httpBasic((httpBasic) -> httpBasic.disable())
				.cors((cors) -> cors.disable())
				.authorizeHttpRequests(request -> request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/user/login", "/", "/user/signup", "/user/isDupleId", "/refresh-token").permitAll()
						.requestMatchers(HttpMethod.GET, "/user/normal/**").hasRole("NORMAL")
						.requestMatchers("/user/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				// .formLogin((formLogin) ->
				// formLogin.loginPage("/login").defaultSuccessUrl("/"))
				.exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))
				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true))
				.sessionManagement((sessionmng) -> sessionmng.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

	private final AuthenticationEntryPoint unauthorizedEntryPoint = (request, response, authException) -> {
		com.attendance.util.ErrorResponse fail = new com.attendance.util.ErrorResponse("401",
				"Spring security unauthorized...");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		String json = new ObjectMapper().writeValueAsString(fail);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

	private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		com.attendance.util.ErrorResponse fail = new com.attendance.util.ErrorResponse("403",
				"Spring security forbidden...");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		String json = new ObjectMapper().writeValueAsString(fail);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

}
