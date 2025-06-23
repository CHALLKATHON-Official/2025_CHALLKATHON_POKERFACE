package com.time.PokerFace.auth.dto;

import com.time.PokerFace.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String username;
    private String email;
    private String profileImageUrl;

    public UserInfoResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
