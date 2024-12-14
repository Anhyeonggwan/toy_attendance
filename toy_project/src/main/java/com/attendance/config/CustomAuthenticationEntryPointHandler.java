package com.attendance.config;

import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * AuthenticationEntryPoint의 구현체 CustomAuthenticationEntryPointHandler를 구현한다. 
 * 이 핸들러는 사용자가 인증되지 않았거나 유효한 인증정보를 가지고 있지 않은 경우 동작하는 클래스이다. 
 * 한마디로 로그인을 하지 않은 사용자가 로그인이 필요한 리소스에 접근할 때 동작한다. 
 * 해당 코드에선 401 관련 내용을 응답한다.
 */
@Component
@Log4j2
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws java.io.IOException {
        log.info("[CustomAuthenticationEntryPointHandler] :: {}", authException.getMessage());
        log.info("[CustomAuthenticationEntryPointHandler] :: {}", request.getRequestURL());
        log.info("[CustomAuthenticationEntryPointHandler] :: 토근 정보가 만료되었거나 존재하지 않음");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("errorCode", "403");
        returnJson.addProperty("errorMsg", "권한이 존재하지 않습니다.");

        PrintWriter out = response.getWriter();
        out.print(returnJson);
    }

}
