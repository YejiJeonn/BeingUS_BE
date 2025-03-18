package com.example.beingus_be.controller;

import com.example.beingus_be.dto.CreateUserRequestDto;
import com.example.beingus_be.repository.UserRepository;
import com.example.beingus_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    public final UserService userService;

    private final UserRepository userRepository;

//    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/users/signup")
    // ResponseEntity<반환 타입> => 요청한 자에게 반환할 값, 지정한 타입의 값만 반환됨
    public ResponseEntity<String> signup(@RequestBody CreateUserRequestDto requestDto) {

        userService.saveUser(requestDto);

        return ResponseEntity.ok("회원가입 성공");
    }

    // 아이디 중복확인
    @PostMapping("/users/check-id")
    public ResponseEntity<Boolean> checkIdDuplicate(@RequestParam String userId) {

        boolean result = userService.isUserIdDuplicate(userId);

        return ResponseEntity.ok(result);
    }
}
