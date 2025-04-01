package com.example.beingus_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestDto {

    private Long id;
    private Long kakaoId;
    private String nickname;
}
