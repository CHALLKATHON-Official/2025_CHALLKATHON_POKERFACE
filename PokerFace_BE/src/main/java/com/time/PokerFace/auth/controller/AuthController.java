package com.time.PokerFace.auth.controller;

import com.time.PokerFace.auth.dto.LoginRequest;
import com.time.PokerFace.auth.dto.SignupRequest;
import com.time.PokerFace.auth.dto.UserInfoResponse;
import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.auth.jwt.JwtTokenProvider;
import com.time.PokerFace.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 간단한 refreshToken 저장소 (실서비스는 DB/Redis 사용 권장)
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest dto) {
        User user = userService.signup(dto);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", new UserInfoResponse(user));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        User user = userService.login(dto);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        refreshTokenStore.put(String.valueOf(user.getId()), refreshToken);

        Map<String, Object> data = new HashMap<>();
        data.put("user", new UserInfoResponse(user));
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "refreshToken required"));
        }
        Long userId;
        try {
            userId = jwtTokenProvider.getUserId(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid refreshToken"));
        }
        String stored = refreshTokenStore.get(String.valueOf(userId));
        if (stored == null || !stored.equals(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid refreshToken"));
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("accessToken", newAccessToken)));
    }
}
