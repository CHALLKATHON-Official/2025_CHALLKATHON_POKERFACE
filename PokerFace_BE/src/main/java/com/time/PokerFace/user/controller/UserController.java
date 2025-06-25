package com.time.PokerFace.user.controller;

import com.time.PokerFace.user.dto.UserSearchResponse;
import com.time.PokerFace.user.dto.UserSearchHistoryResponse;
import com.time.PokerFace.user.dto.UserSearchSuggestResponse;
import com.time.PokerFace.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(@RequestParam String query) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        List<UserSearchResponse> results = userService.searchUsers(userId, query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/history")
    public ResponseEntity<List<UserSearchHistoryResponse>> getSearchHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        List<UserSearchHistoryResponse> history = userService.getSearchHistory(userId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/search/history/{id}")
    public ResponseEntity<Void> deleteSearchHistory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        userService.deleteSearchHistory(userId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/suggest")
    public ResponseEntity<UserSearchSuggestResponse> suggestKeywords(@RequestParam(required = false) String prefix) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        UserSearchSuggestResponse response = userService.suggestKeywords(userId, prefix);
        return ResponseEntity.ok(response);
    }
} 