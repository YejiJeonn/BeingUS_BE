package com.example.beingus_be.service;

import com.example.beingus_be.dto.CreateUserRequestDto;
import com.example.beingus_be.entity.User;
import com.example.beingus_be.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.example.beingus_be.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;    // 비밀번호 암호화

    @Autowired
    private TokenProvider tokenProvider;

    // 회원가입 시 정보저장 로직
    public void saveUser(CreateUserRequestDto dto) {

        User user = new User();
        user.setKakaoId(dto.getKakaoId());
        user.setNickname(dto.getNickname());

        userRepository.save(user);
    }

//    // 회원가입 시 아이디 중복확인
//    public boolean isUserIdDuplicate(String userId) {
//
//        return userRepository.existsByUserId(userId);
//    }

    // 카카오 토큰 이용 JWT 발급
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
            .build();

    public Map<String, Object> getUserInfoFromKakao(String accessToken) {
        System.out.println("accessToken: " + accessToken);

        return webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();   // block()을 써서 동기 처리
    }
}
