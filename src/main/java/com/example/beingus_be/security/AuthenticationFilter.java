package com.example.beingus_be.security;

import com.example.beingus_be.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 토큰 인증 필터
 * <p>
 * 헤더에서 토큰을 가져와서 유효한 토큰인지 확인하고,
 * 유효하다면 SecurityContextHolder에 Authentication 객체를 저장하고 다음 필터로 넘어가도록 함
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // HTTP 요청을 가로채고 토큰을 확인하는 역할
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 쿠키에서 JWT 찾기
        String jwt = extractTokenFromCookie(request);

        // 2. JWT 유효성 검사
        if (jwt != null && tokenProvider.validateToken(jwt)) {
            String userId = tokenProvider.createToken(jwt);
            Authentication auth = tokenProvider.getAuthentication(userId);

            // 3. 인증객체 생성 후 SecurityContext에 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(jwt, null, null); // 권한 필요 시 추가
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if("Authorization".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

