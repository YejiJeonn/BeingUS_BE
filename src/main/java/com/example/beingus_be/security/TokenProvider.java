package com.example.beingus_be.security;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("jeonjeonjeonjeonjeonjeonjeonjeonjeonjeon")
    private String secretKey;

    protected SecretKey key;

    @PostConstruct
    protected void init() {
        key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    }

    // 사용자 ID를 기반으로 인증 객체 반환
    public Authentication getAuthentication(String kakaoId) {
        return CustomAuthenticationToken.builder()
                .principal(kakaoId) // 이제 String
                .authorities(Collections.emptyList())
                .credentials(null)
                .build();
    }

    // 현재 인증된 사용자 ID (principal) 가져오기
    public String getIdFormAuthentication() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // JWT 생성 (subject = kakaoId)
    public String createToken(String kakaoId) {
        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .subject(kakaoId)
                .signWith(key)
                .compact();
    }

    // 토큰에서 사용자 ID(kakaoId) 추출
    public String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // JWT 파싱 성공여부 반환
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

