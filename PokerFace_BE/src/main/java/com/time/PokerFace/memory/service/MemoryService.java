package com.time.PokerFace.memory.service;

import com.time.PokerFace.memory.dto.MemoryUploadRequest;
import com.time.PokerFace.memory.dto.MemoryResponse;
import com.time.PokerFace.memory.dto.MemoryListItem;
import com.time.PokerFace.memory.dto.MemoryListResponse;
import com.time.PokerFace.memory.dto.MemoryDetailResponse;
import com.time.PokerFace.memory.dto.MemoryUpdateRequest;
import com.time.PokerFace.memory.dto.MemoryFilterRequest;
import com.time.PokerFace.memory.dto.MemoryRecommendRequest;
import com.time.PokerFace.memory.dto.RandomMemoryRequest;
import com.time.PokerFace.memory.dto.RandomMemoryResponse;
import com.time.PokerFace.memory.entity.Emotion;
import com.time.PokerFace.memory.entity.Memory;
import com.time.PokerFace.memory.repository.MemoryRepository;
import com.time.PokerFace.like.entity.MemoryLike;
import com.time.PokerFace.like.repository.MemoryLikeRepository;
import com.time.PokerFace.bookmark.entity.MemoryBookmark;
import com.time.PokerFace.bookmark.repository.MemoryBookmarkRepository;
import com.time.PokerFace.comment.service.CommentService;
import com.time.PokerFace.common.service.S3Uploader;
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
import java.util.Random;

@Service
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final MemoryLikeRepository memoryLikeRepository;
    private final MemoryBookmarkRepository memoryBookmarkRepository;
    private final CommentService commentService;
    private final S3Uploader s3Uploader;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository, MemoryLikeRepository memoryLikeRepository, MemoryBookmarkRepository memoryBookmarkRepository, CommentService commentService, S3Uploader s3Uploader) {
        this.memoryRepository = memoryRepository;
        this.memoryLikeRepository = memoryLikeRepository;
        this.memoryBookmarkRepository = memoryBookmarkRepository;
        this.commentService = commentService;
        this.s3Uploader = s3Uploader;
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

    public MemoryListResponse filterMemories(MemoryFilterRequest request, Long userId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Memory> memoryPage;
        
        // 감정 필터링
        if (request.getEmotion() != null && !request.getEmotion().isEmpty()) {
            Emotion emotion = Emotion.valueOf(request.getEmotion().toUpperCase());
            if (userId != null) {
                memoryPage = memoryRepository.findByEmotionAndUserIdOrderByCreatedAtDesc(emotion, userId, pageable);
            } else {
                memoryPage = memoryRepository.findByEmotionOrderByCreatedAtDesc(emotion, pageable);
            }
        } else {
            // 감정 필터가 없으면 전체 조회
            if (userId != null) {
                memoryPage = memoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
            } else {
                memoryPage = memoryRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }
        
        // TODO: 카테고리, 태그 필터링 추가 (추후 확장)
        
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
        response.setPage(request.getPage());
        response.setSize(request.getSize());
        return response;
    }

    public MemoryListResponse getRecommendedMemories(MemoryRecommendRequest request) {
        if (request.getEmotion() == null || request.getEmotion().isEmpty()) {
            throw new RuntimeException("Emotion is required");
        }
        
        Emotion emotion = Emotion.valueOf(request.getEmotion().toUpperCase());
        Pageable pageable = PageRequest.of(0, request.getSize());
        List<Memory> memories;
        
        switch (request.getType().toLowerCase()) {
            case "popular":
                // 인기 썰 (공감수 높은 순) - 현재는 최신순으로 대체 (공감수 정렬은 추후)
                memories = memoryRepository.findByEmotionOrderByCreatedAtDesc(emotion, pageable);
                break;
            case "recent":
                // 최신 썰
                memories = memoryRepository.findByEmotionOrderByCreatedAtDesc(emotion, pageable);
                break;
            case "random":
                // 랜덤 썰
                List<Memory> allMemories = memoryRepository.findByEmotion(emotion);
                if (allMemories.size() <= request.getSize()) {
                    memories = allMemories;
                } else {
                    Collections.shuffle(allMemories);
                    memories = allMemories.subList(0, request.getSize());
                }
                break;
            default:
                throw new RuntimeException("Invalid recommendation type");
        }
        
        List<MemoryListItem> items = new ArrayList<>();
        for (Memory m : memories) {
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
        response.setTotalPages(1);
        response.setTotalElements(items.size());
        response.setPage(0);
        response.setSize(items.size());
        return response;
    }

    public RandomMemoryResponse getRandomMemories(RandomMemoryRequest request) {
        List<Memory> memories;
        int count = Math.min(request.getCount(), 10); // 최대 10개로 제한
        
        if (request.getEmotion() != null && request.getCategory() != null) {
            // 감정과 카테고리 모두 지정된 경우
            memories = memoryRepository.findRandomMemoriesByEmotionAndCategory(
                request.getEmotion().toUpperCase(), 
                request.getCategory().toUpperCase(), 
                count
            );
        } else if (request.getEmotion() != null) {
            // 감정만 지정된 경우
            memories = memoryRepository.findRandomMemoriesByEmotion(
                request.getEmotion().toUpperCase(), 
                count
            );
        } else if (request.getCategory() != null) {
            // 카테고리만 지정된 경우
            memories = memoryRepository.findRandomMemoriesByCategory(
                request.getCategory().toUpperCase(), 
                count
            );
        } else {
            // 아무 조건 없이 랜덤
            memories = memoryRepository.findRandomMemories(count);
        }
        
        List<RandomMemoryResponse.RandomMemoryItem> items = new ArrayList<>();
        for (Memory memory : memories) {
            RandomMemoryResponse.RandomMemoryItem item = new RandomMemoryResponse.RandomMemoryItem();
            item.setId(memory.getId());
            item.setContent(memory.getContent());
            item.setEmotion(memory.getEmotion() != null ? memory.getEmotion().name() : null);
            item.setImageUrl(memory.getImageUrl());
            item.setUserId(memory.getUserId());
            item.setCreatedAt(memory.getCreatedAt() != null ? 
                memory.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            item.setLikeCount(getLikeCount(memory.getId()));
            items.add(item);
        }
        
        RandomMemoryResponse response = new RandomMemoryResponse();
        response.setMemories(items);
        response.setTotalCount(items.size());
        return response;
    }
} 