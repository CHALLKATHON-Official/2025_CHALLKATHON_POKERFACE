package com.time.PokerFace.auth.controller;

import com.time.PokerFace.auth.dto.EmailChangeRequest;
import com.time.PokerFace.auth.dto.PasswordChangeRequest;
import com.time.PokerFace.auth.dto.ProfileUpdateRequest;
import com.time.PokerFace.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
public class AuthUserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUsername()));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody PasswordChangeRequest dto) {
        userService.updatePassword(userDetails.getUsername(), dto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PatchMapping("/email")
    public ResponseEntity<?> changeEmail(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody EmailChangeRequest dto) {
        userService.updateEmail(userDetails.getUsername(), dto);
        return ResponseEntity.ok("이메일 변경 완료");
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody ProfileUpdateRequest dto) {
        userService.updateProfile(userDetails.getUsername(), dto);
        return ResponseEntity.ok("프로필 업데이트 완료");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}
