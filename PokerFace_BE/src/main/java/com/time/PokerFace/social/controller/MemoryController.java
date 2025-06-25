package com.time.PokerFace.social.controller;

import com.time.PokerFace.social.dto.MemoryUploadRequest;
import com.time.PokerFace.social.dto.MemoryResponse;
import com.time.PokerFace.social.dto.MemoryListResponse;
import com.time.PokerFace.social.dto.MemoryDetailResponse;
import com.time.PokerFace.social.dto.MemoryUpdateRequest;
import com.time.PokerFace.social.dto.CommentRequest;
import com.time.PokerFace.social.dto.CommentResponse;
import com.time.PokerFace.social.service.MemoryService;
import com.time.PokerFace.social.service.CommentService;
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
    private final CommentService commentService;

    @Autowired
    public MemoryController(MemoryService memoryService, CommentService commentService) {
        this.memoryService = memoryService;
        this.commentService = commentService;
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

    @GetMapping("/{id}")
    public ResponseEntity<MemoryDetailResponse> getMemoryDetail(@PathVariable Long id) {
        MemoryDetailResponse response = memoryService.getMemoryDetail(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<MemoryResponse> updateMemory(
            @PathVariable Long id,
            @ModelAttribute MemoryUpdateRequest request
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        MemoryResponse response = memoryService.updateMemory(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        memoryService.deleteMemory(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        memoryService.addLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> removeLike(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        memoryService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        CommentResponse response = commentService.addComment(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
        List<CommentResponse> responses = commentService.getComments(id);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<Void> addBookmark(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        memoryService.addBookmark(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/bookmark")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        memoryService.removeBookmark(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<MemoryListResponse> getBookmarkedMemories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        MemoryListResponse response = memoryService.getBookmarkedMemories(userId, page, size);
        return ResponseEntity.ok(response);
    }
} 