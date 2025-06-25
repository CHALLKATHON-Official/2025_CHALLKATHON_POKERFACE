package com.time.PokerFace.social.service;

import com.time.PokerFace.social.dto.MemoryUploadRequest;
import com.time.PokerFace.social.dto.MemoryResponse;
import com.time.PokerFace.social.dto.MemoryListItem;
import com.time.PokerFace.social.dto.MemoryListResponse;
import com.time.PokerFace.social.dto.MemoryDetailResponse;
import com.time.PokerFace.social.dto.MemoryUpdateRequest;
import com.time.PokerFace.social.dto.MyRoomResponse;
import com.time.PokerFace.social.entity.Emotion;
import com.time.PokerFace.social.entity.Memory;
import com.time.PokerFace.social.entity.MemoryLike;
import com.time.PokerFace.social.entity.MemoryBookmark;
import com.time.PokerFace.social.repository.MemoryRepository;
import com.time.PokerFace.social.repository.MemoryLikeRepository;
import com.time.PokerFace.social.repository.MemoryBookmarkRepository;
import com.time.PokerFace.social.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final S3Uploader s3Uploader;
    private final MemoryLikeRepository memoryLikeRepository;
    private final MemoryBookmarkRepository memoryBookmarkRepository;
    private final CommentService commentService;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository, S3Uploader s3Uploader, MemoryLikeRepository memoryLikeRepository, MemoryBookmarkRepository memoryBookmarkRepository, CommentService commentService) {
        this.memoryRepository = memoryRepository;
        this.s3Uploader = s3Uploader;
        this.memoryLikeRepository = memoryLikeRepository;
        this.memoryBookmarkRepository = memoryBookmarkRepository;
        this.commentService = commentService;
    }

    public MemoryResponse uploadMemory(Long userId, MemoryUploadRequest request) throws IOException {
        String imageUrl = null;
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Uploader.upload(image, "memories");
        }

        Emotion emotion = Emotion.valueOf(request.getEmotion().toUpperCase());

        Memory memory = new Memory();
        memory.setUserId(userId);
        memory.setContent(request.getContent());
        memory.setEmotion(emotion);
        memory.setImageUrl(imageUrl);

        Memory saved = memoryRepository.save(memory);

        MemoryResponse response = new MemoryResponse();
        response.setId(saved.getId());
        response.setContent(saved.getContent());
        response.setEmotion(saved.getEmotion().name());
        response.setImageUrl(saved.getImageUrl());
        response.setCreatedAt(saved.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    public MemoryListResponse getMemories(String type, Long userId, int page, int size, List<Long> followingUserIds) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Memory> memoryPage;
        if ("following".equalsIgnoreCase(type) && followingUserIds != null && !followingUserIds.isEmpty()) {
            memoryPage = memoryRepository.findByUserIdInOrderByCreatedAtDesc(followingUserIds, pageable);
        } else {
            // 인기 썰은 추후 공감수 정렬, 현재는 최신과 동일
            memoryPage = memoryRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        List<MemoryListItem> items = new ArrayList<>();
        for (Memory m : memoryPage.getContent()) {
            MemoryListItem item = new MemoryListItem();
            item.setId(m.getId());
            item.setContent(m.getContent());
            item.setEmotion(m.getEmotion() != null ? m.getEmotion().name() : null);
            item.setImageUrl(m.getImageUrl());
            item.setCreatedAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null);
            item.setUserId(m.getUserId());
            item.setLikes(getLikeCount(m.getId()));
            items.add(item);
        }
        MemoryListResponse response = new MemoryListResponse();
        response.setMemories(items);
        response.setTotalPages(memoryPage.getTotalPages());
        response.setTotalElements(memoryPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        return response;
    }

    public MemoryDetailResponse getMemoryDetail(Long id) {
        Optional<Memory> optional = memoryRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("Memory not found");
        }
        Memory m = optional.get();
        MemoryDetailResponse response = new MemoryDetailResponse();
        response.setId(m.getId());
        response.setContent(m.getContent());
        response.setEmotion(m.getEmotion() != null ? m.getEmotion().name() : null);
        response.setImageUrl(m.getImageUrl());
        response.setCreatedAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null);
        response.setUserId(m.getUserId());
        response.setLikes(getLikeCount(m.getId()));
        response.setComments(commentService.getComments(m.getId()));
        return response;
    }

    public MemoryResponse updateMemory(Long id, Long userId, MemoryUpdateRequest request) throws IOException {
        Memory memory = memoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Memory not found"));
        if (!memory.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to update this memory");
        }
        if (request.getContent() != null) memory.setContent(request.getContent());
        if (request.getEmotion() != null) memory.setEmotion(Emotion.valueOf(request.getEmotion().toUpperCase()));
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = s3Uploader.upload(request.getImage(), "memories");
            memory.setImageUrl(imageUrl);
        }
        Memory saved = memoryRepository.save(memory);
        MemoryResponse response = new MemoryResponse();
        response.setId(saved.getId());
        response.setContent(saved.getContent());
        response.setEmotion(saved.getEmotion().name());
        response.setImageUrl(saved.getImageUrl());
        response.setCreatedAt(saved.getCreatedAt().toString());
        return response;
    }

    public void deleteMemory(Long id, Long userId) {
        Memory memory = memoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Memory not found"));
        if (!memory.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to delete this memory");
        }
        memoryRepository.delete(memory);
    }

    public void addLike(Long memoryId, Long userId) {
        if (memoryLikeRepository.findByMemoryIdAndUserId(memoryId, userId).isPresent()) {
            throw new RuntimeException("Already liked");
        }
        MemoryLike like = new MemoryLike();
        like.setMemoryId(memoryId);
        like.setUserId(userId);
        memoryLikeRepository.save(like);
    }

    public void removeLike(Long memoryId, Long userId) {
        memoryLikeRepository.deleteByMemoryIdAndUserId(memoryId, userId);
    }

    public int getLikeCount(Long memoryId) {
        return memoryLikeRepository.countByMemoryId(memoryId);
    }

    public void addBookmark(Long memoryId, Long userId) {
        if (memoryBookmarkRepository.findByMemoryIdAndUserId(memoryId, userId).isPresent()) {
            throw new RuntimeException("Already bookmarked");
        }
        MemoryBookmark bookmark = new MemoryBookmark();
        bookmark.setMemoryId(memoryId);
        bookmark.setUserId(userId);
        memoryBookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long memoryId, Long userId) {
        memoryBookmarkRepository.deleteByMemoryIdAndUserId(memoryId, userId);
    }

    public MemoryListResponse getBookmarkedMemories(Long userId, int page, int size) {
        List<MemoryBookmark> bookmarks = memoryBookmarkRepository.findByUserId(userId);
        List<Long> memoryIds = new ArrayList<>();
        for (MemoryBookmark b : bookmarks) {
            memoryIds.add(b.getMemoryId());
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Memory> memoryPage = memoryRepository.findAllByIdInOrderByCreatedAtDesc(memoryIds, pageable);
        List<MemoryListItem> items = new ArrayList<>();
        for (Memory m : memoryPage.getContent()) {
            MemoryListItem item = new MemoryListItem();
            item.setId(m.getId());
            item.setContent(m.getContent());
            item.setEmotion(m.getEmotion() != null ? m.getEmotion().name() : null);
            item.setImageUrl(m.getImageUrl());
            item.setCreatedAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null);
            item.setUserId(m.getUserId());
            item.setLikes(getLikeCount(m.getId()));
            items.add(item);
        }
        MemoryListResponse response = new MemoryListResponse();
        response.setMemories(items);
        response.setTotalPages(memoryPage.getTotalPages());
        response.setTotalElements(memoryPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        return response;
    }

    public MyRoomResponse getMyMemories(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Memory> memoryPage = memoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<MemoryListItem> items = new ArrayList<>();
        for (Memory m : memoryPage.getContent()) {
            MemoryListItem item = new MemoryListItem();
            item.setId(m.getId());
            item.setContent(m.getContent());
            item.setEmotion(m.getEmotion() != null ? m.getEmotion().name() : null);
            item.setImageUrl(m.getImageUrl());
            item.setCreatedAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null);
            item.setUserId(m.getUserId());
            item.setLikes(getLikeCount(m.getId()));
            items.add(item);
        }
        MyRoomResponse response = new MyRoomResponse();
        response.setMemories(items);
        response.setTotalPages(memoryPage.getTotalPages());
        response.setTotalElements(memoryPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        return response;
    }
} 