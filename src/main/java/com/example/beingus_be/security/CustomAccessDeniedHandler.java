package com.example.beingus_be.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 접근 거부 핸들러
 * <p>
 * 권한이 없는 사용자가 특정 리소스에 접근할 때 (403 Forbidden)
 */
@Component
@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access Denied Handler {}", request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
