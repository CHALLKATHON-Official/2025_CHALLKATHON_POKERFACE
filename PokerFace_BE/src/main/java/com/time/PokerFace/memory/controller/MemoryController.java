package com.time.PokerFace.memory.controller;

import com.time.PokerFace.memory.dto.MemoryUploadRequest;
import com.time.PokerFace.memory.dto.MemoryResponse;
import com.time.PokerFace.memory.dto.MemoryListResponse;
import com.time.PokerFace.memory.dto.MemoryDetailResponse;
import com.time.PokerFace.memory.dto.MemoryUpdateRequest;
import com.time.PokerFace.memory.dto.MemoryFilterRequest;
import com.time.PokerFace.memory.service.MemoryService;
import com.time.PokerFace.comment.dto.CommentRequest;
import com.time.PokerFace.comment.dto.CommentResponse;
import com.time.PokerFace.comment.service.CommentService;
import com.time.PokerFace.myroom.dto.MyRoomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {
    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @GetMapping("/filter")
    public ResponseEntity<MemoryListResponse> filterMemories(@ModelAttribute MemoryFilterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        }
        MemoryListResponse response = memoryService.filterMemories(request, userId);
        return ResponseEntity.ok(response);
    }

    // ... existing code ...
} 