package com.time.PokerFace.social.controller;

import com.time.PokerFace.social.dto.MemoryUploadRequest;
import com.time.PokerFace.social.dto.MemoryResponse;
import com.time.PokerFace.social.dto.MemoryListResponse;
import com.time.PokerFace.social.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {
    private final MemoryService memoryService;

    @Autowired
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MemoryResponse> uploadMemory(
            @ModelAttribute MemoryUploadRequest request
    ) throws IOException {
        // 예시: 인증 유저의 userId 추출 (실제 구현에 맞게 수정 필요)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            // 예시: username을 userId(Long)로 변환하는 로직 필요
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제로는 username이 userId가 아닐 수 있으니, 실제 구현에 맞게 수정 필요
        } else {
            // 인증 정보가 없으면 401 반환
            return ResponseEntity.status(401).build();
        }

        MemoryResponse response = memoryService.uploadMemory(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<MemoryListResponse> getMemories(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 인증 유저 userId 추출 (예시)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        }
        // 팔로우 유저 id 리스트 예시 (실제 연동 필요)
        List<Long> followingUserIds = Arrays.asList(2L, 3L); // TODO: 실제 팔로우 유저 id로 대체
        MemoryListResponse response = memoryService.getMemories(type, userId, page, size, followingUserIds);
        return ResponseEntity.ok(response);
    }
} 