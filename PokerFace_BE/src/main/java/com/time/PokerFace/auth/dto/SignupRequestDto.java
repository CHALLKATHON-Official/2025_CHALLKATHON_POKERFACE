package com.time.PokerFace.auth.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String username;
    private String email;
    private String password;
}
