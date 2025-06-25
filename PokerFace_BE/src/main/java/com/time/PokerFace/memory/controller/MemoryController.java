package com.time.PokerFace.memory.controller;

import com.time.PokerFace.memory.dto.MemoryUploadRequest;
import com.time.PokerFace.memory.dto.MemoryResponse;
import com.time.PokerFace.memory.dto.MemoryListResponse;
import com.time.PokerFace.memory.dto.MemoryDetailResponse;
import com.time.PokerFace.memory.dto.MemoryUpdateRequest;
import com.time.PokerFace.memory.dto.MemoryFilterRequest;
import com.time.PokerFace.memory.dto.MemoryRecommendRequest;
import com.time.PokerFace.memory.dto.RandomMemoryRequest;
import com.time.PokerFace.memory.dto.RandomMemoryResponse;
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
    private final CommentService commentService;

    public MemoryController(MemoryService memoryService, CommentService commentService) {
        this.memoryService = memoryService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<MemoryResponse> uploadMemory(@ModelAttribute MemoryUploadRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            MemoryResponse response = memoryService.uploadMemory(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<MemoryListResponse> getMemories(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MemoryListResponse response = memoryService.getMemories(type, userId, page, size, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryDetailResponse> getMemoryDetail(@PathVariable Long id) {
        MemoryDetailResponse response = memoryService.getMemoryDetail(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoryResponse> updateMemory(@PathVariable Long id, @ModelAttribute MemoryUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            MemoryResponse response = memoryService.updateMemory(id, userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            memoryService.deleteMemory(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            memoryService.addLike(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> removeLike(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            memoryService.removeLike(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<Void> addBookmark(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            memoryService.addBookmark(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/bookmark")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try {
            memoryService.removeBookmark(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<MemoryListResponse> getBookmarkedMemories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MemoryListResponse response = memoryService.getBookmarkedMemories(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        request.setMemoryId(id);
        request.setUserId(userId);
        try {
            commentService.createComment(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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

    @GetMapping("/recommend")
    public ResponseEntity<MemoryListResponse> getRecommendedMemories(@ModelAttribute MemoryRecommendRequest request) {
        MemoryListResponse response = memoryService.getRecommendedMemories(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random")
    public ResponseEntity<RandomMemoryResponse> getRandomMemories(@ModelAttribute RandomMemoryRequest request) {
        RandomMemoryResponse response = memoryService.getRandomMemories(request);
        return ResponseEntity.ok(response);
    }
} 