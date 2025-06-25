package com.time.PokerFace.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private String email;
    private String password;
    private String profileImage;
    private String provider;

    // 기타 필드 및 메서드

    public String getNickname() { return nickname; }
    public String getProfileImage() { return profileImage; }
} 