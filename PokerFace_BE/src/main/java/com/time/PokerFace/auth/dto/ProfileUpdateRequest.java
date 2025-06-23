package com.time.PokerFace.auth.dto;

import lombok.Getter;

@Getter
public class ProfileUpdateRequest {
    private String newUsername;
    private String profileImageUrl;
}
