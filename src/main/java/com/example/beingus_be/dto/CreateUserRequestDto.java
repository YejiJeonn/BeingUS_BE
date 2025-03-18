package com.example.beingus_be.dto;

import lombok.Getter;

@Getter
public class CreateUserRequestDto {

    private String userId;
    private String password;
    private String name;
    private String phoneNumber;
    private String recommandId;
}
