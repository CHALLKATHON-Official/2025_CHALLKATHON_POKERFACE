package com.time.PokerFace.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일

    public String createAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public Long getUserId(String token) {
        String subject = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 