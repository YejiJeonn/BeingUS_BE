package com.example.beingus_be.controller;

import com.example.beingus_be.dto.CreateUserRequestDto;
import com.example.beingus_be.repository.UserRepository;
import com.example.beingus_be.security.TokenProvider;
import com.example.beingus_be.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public final UserService userService;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

//    // 회원가입
//    @PostMapping("/signup")
//    // ResponseEntity<반환 타입> => 요청한 자에게 반환할 값, 지정한 타입의 값만 반환됨
//    public ResponseEntity<String> signup(@RequestBody CreateUserRequestDto request) {
//
//        userService.saveUser(request);
//
//        return ResponseEntity.ok("회원가입 성공");
//    }
//
//    // 아이디 중복확인
//    @PostMapping("/check-id")
//    public ResponseEntity<Boolean> checkIdDuplicate(@RequestParam String userId) {
//
//        boolean result = userService.isUserIdDuplicate(userId); // 아이디 없으면 flase(중복x), 있으면 true(중복o)
//
//        return ResponseEntity.ok(result);
//    }

    // 카카오 토큰 이용 JWT 발급
    @PostMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin(@RequestParam String token, HttpServletResponse response) {

        // 1. WebClient로 사용자 정보 요청
        Map<String, Object> kakaoUser = userService.getUserInfoFromKakao(token);

        // 2. 사용자 정보 파싱
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUser.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) kakaoUser.get("properties");

        Long kakaoId = (Long) kakaoUser.get("id");
        String nickname = properties.get("nickname").toString();

        // 3. (필요 시) 사용자 DB 저장 or 조회
        if (!userRepository.existsById(Long.valueOf(kakaoId))) {

            CreateUserRequestDto dto = new CreateUserRequestDto();
            dto.setKakaoId(kakaoId);
            dto.setNickname(nickname);

            userService.saveUser(dto);
        }

        // 4. JWT 토큰 발급
        String jwt = tokenProvider.createToken(kakaoId.toString());

        // 5. JWT 토큰 쿠키에 저장
        Cookie cookie = new Cookie("Authorization", jwt);
        cookie.setHttpOnly(true);   // 자바스크립트에서 쿠키 접근 불가
        cookie.setSecure(true); // https에서만 쿠키 전송
        cookie.setPath("/");    // 전체 경로에 적용
        cookie.setMaxAge(60 * 60); // 1시간

        response.addCookie(cookie);

        return ResponseEntity.ok("카카오 로그인 성공");
    }

}
