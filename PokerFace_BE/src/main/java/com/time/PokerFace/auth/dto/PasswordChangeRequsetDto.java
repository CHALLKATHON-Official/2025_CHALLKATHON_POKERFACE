package com.time.PokerFace.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequsetDto {
    private String currentPassword;
    private String newPassword;
}
