package com.time.PokerFace.auth.controller;

import com.time.PokerFace.auth.dto.LoginRequestDto;
import com.time.PokerFace.auth.dto.SignupRequestDto;
import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        User user = userService.login(dto);
        return ResponseEntity.ok("로그인 성공: " + user.getUsername());
    }
}
