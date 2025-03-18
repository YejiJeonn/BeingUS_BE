package com.example.beingus_be.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 토큰 인증 필터
 * <p>
 * 헤더에서 토큰을 가져와서 유효한 토큰인지 확인하고,
 * 유효하다면 SecurityContextHolder에 Authentication 객체를 저장하고 다음 필터로 넘어가도록 함
 */
@Log4j2
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

        // AUTHORIZATION 헤더에서 Bearer Token 추출
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accessToken != null) {
            accessToken = accessToken.substring(7);
        }

        // 토큰에서 사용자 id 추출
        Long id = tokenProvider.getSubject(accessToken);

        // 토큰이 유효하면 Authentication 객체 생성
        if (id != null) {
            // id 이용하여 Authentication 객체를 생성
            Authentication authentication = tokenProvider.getAuthentication(id);

            // SecurityContextHolder에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }
}
