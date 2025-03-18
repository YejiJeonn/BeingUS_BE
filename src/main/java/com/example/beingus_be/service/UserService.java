package com.example.beingus_be.service;

import com.example.beingus_be.dto.CreateUserRequestDto;
import com.example.beingus_be.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.beingus_be.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;    // 비밀번호 암호화

//    @Autowired
//    private TokenProvider tokenProvider;

    // 회원가입 시 정보저장 로직
    public void saveUser(CreateUserRequestDto dto) {

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호 암호화
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRecommandId(dto.getRecommandId());

        userRepository.save(user);
    }

    // 회원가입 시 아이디 중복확인
    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

}
