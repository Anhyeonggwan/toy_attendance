package com.attendance.config;

import java.io.PrintWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*
	 * 코드 설명 csrf : csrf(Cross site Request forgery) 설정을 disable 하였습니다. HttpBasic(),
	 * FormLogin() : Json을 통해 로그인을 진행하는데, 로그인 이후 refresh 토큰이 만료되기 전까지 토큰을 통한 인증을
	 * 진행할것 이기 때문에 비활성화 하였습니다. httpBasic : Http basic Auth 기반으로 로그인 인증창이 뜬다.
	 * 
	 * 
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf((csrfConfig) -> csrfConfig.disable()).httpBasic((httpBasic) -> httpBasic.disable())
				.cors((cors) -> cors.disable())
				.authorizeHttpRequests(request -> request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/login", "/", "/signup").permitAll().requestMatchers("/user/normal/**")
						.hasRole("USER").requestMatchers("/user/admin/**").hasRole("ADMIN").anyRequest()
						.authenticated())
				// .formLogin((formLogin) ->
				// formLogin.loginPage("/login").defaultSuccessUrl("/"))
				.exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))
				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true));
		return http.build();
	}

	private final AuthenticationEntryPoint unauthorizedEntryPoint = (request, response, authException) -> {
		com.attendance.util.ErrorResponse fail = new com.attendance.util.ErrorResponse(HttpStatus.UNAUTHORIZED,
				"Spring security unauthorized...");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		String json = new ObjectMapper().writeValueAsString(fail);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

	private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		com.attendance.util.ErrorResponse fail = new com.attendance.util.ErrorResponse(HttpStatus.FORBIDDEN,
				"Spring security forbidden...");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		String json = new ObjectMapper().writeValueAsString(fail);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

}
