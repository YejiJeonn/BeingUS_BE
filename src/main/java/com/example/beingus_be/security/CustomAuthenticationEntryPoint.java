package com.example.beingus_be.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 진입점
 * <p>
 * 인증되지 않은 사용자가 보호된 리소스에 액세스하려고 할 때 (401 Unauthorized)
 */
@Log4j2
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("[인증 실패] 요청 URI: {}", request.getRequestURI());
        log.warn("[예외 메시지] {}",authException.getMessage());

        // 응답 구성
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // 401
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"error\" : \"인증이 필요한 요청입니다.\"}");
    }
}
