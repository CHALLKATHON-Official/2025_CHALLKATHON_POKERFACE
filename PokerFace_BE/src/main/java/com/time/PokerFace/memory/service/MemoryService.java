package com.time.PokerFace.memory.service;

import com.time.PokerFace.memory.dto.MemoryUploadRequest;
import com.time.PokerFace.memory.dto.MemoryResponse;
import com.time.PokerFace.memory.dto.MemoryListItem;
import com.time.PokerFace.memory.dto.MemoryListResponse;
import com.time.PokerFace.memory.dto.MemoryDetailResponse;
import com.time.PokerFace.memory.dto.MemoryUpdateRequest;
import com.time.PokerFace.memory.dto.MyRoomResponse;
import com.time.PokerFace.memory.entity.Emotion;
import com.time.PokerFace.memory.entity.Memory;
import com.time.PokerFace.memory.repository.MemoryRepository;
import com.time.PokerFace.like.entity.MemoryLike;
import com.time.PokerFace.like.repository.MemoryLikeRepository;
import com.time.PokerFace.bookmark.entity.MemoryBookmark;
import com.time.PokerFace.bookmark.repository.MemoryBookmarkRepository;
import com.time.PokerFace.comment.service.CommentService;
import com.time.PokerFace.common.service.S3Uploader;

import java.util.List;
import java.util.Optional;

public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final MemoryLikeRepository memoryLikeRepository;
    private final MemoryBookmarkRepository memoryBookmarkRepository;
    private final CommentService commentService;
    private final S3Uploader s3Uploader;

    public MemoryService(MemoryRepository memoryRepository, MemoryLikeRepository memoryLikeRepository, MemoryBookmarkRepository memoryBookmarkRepository, CommentService commentService, S3Uploader s3Uploader) {
        this.memoryRepository = memoryRepository;
        this.memoryLikeRepository = memoryLikeRepository;
        this.memoryBookmarkRepository = memoryBookmarkRepository;
        this.commentService = commentService;
        this.s3Uploader = s3Uploader;
    }

    public List<Memory> getAllMemories() {
        return memoryRepository.findAll();
    }

    public Optional<Memory> getMemoryById(Long id) {
        return memoryRepository.findById(id);
    }

    public Memory saveMemory(Memory memory) {
        return memoryRepository.save(memory);
    }

    public void deleteMemory(Long id) {
        memoryRepository.deleteById(id);
    }

    public List<MemoryLike> getAllMemoryLikes() {
        return memoryLikeRepository.findAll();
    }

    public List<MemoryBookmark> getAllMemoryBookmarks() {
        return memoryBookmarkRepository.findAll();
    }

    public MemoryLike saveMemoryLike(MemoryLike memoryLike) {
        return memoryLikeRepository.save(memoryLike);
    }

    public MemoryBookmark saveMemoryBookmark(MemoryBookmark memoryBookmark) {
        return memoryBookmarkRepository.save(memoryBookmark);
    }

    public void deleteMemoryLike(Long id) {
        memoryLikeRepository.deleteById(id);
    }

    public void deleteMemoryBookmark(Long id) {
        memoryBookmarkRepository.deleteById(id);
    }

    public List<Memory> getMemoriesByUserId(Long userId) {
        return memoryRepository.findByUserId(userId);
    }

    public List<Memory> getMemoriesByRoomId(Long roomId) {
        return memoryRepository.findByRoomId(roomId);
    }

    public List<Memory> getMemoriesByEmotion(Emotion emotion) {
        return memoryRepository.findByEmotion(emotion);
    }

    public List<Memory> getMemoriesByKeyword(String keyword) {
        return memoryRepository.findByKeyword(keyword);
    }

    public List<Memory> getMemoriesByDate(String date) {
        return memoryRepository.findByDate(date);
    }

    public List<Memory> getMemoriesByDateRange(String startDate, String endDate) {
        return memoryRepository.findByDateRange(startDate, endDate);
    }

    public List<Memory> getMemoriesByDateAndKeyword(String date, String keyword) {
        return memoryRepository.findByDateAndKeyword(date, keyword);
    }

    public List<Memory> getMemoriesByDateAndEmotion(String date, Emotion emotion) {
        return memoryRepository.findByDateAndEmotion(date, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoom(String date, Long roomId) {
        return memoryRepository.findByDateAndRoom(date, roomId);
    }

    public List<Memory> getMemoriesByDateAndRoomAndKeyword(String date, Long roomId, String keyword) {
        return memoryRepository.findByDateAndRoomAndKeyword(date, roomId, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndEmotion(String date, Long roomId, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndEmotion(date, roomId, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateRange(String date, Long roomId, String startDate, String endDate) {
        return memoryRepository.findByDateAndRoomAndDateRange(date, roomId, startDate, endDate);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndKeyword(String date, Long roomId, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndKeyword(date, roomId, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndEmotion(String date, Long roomId, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndEmotion(date, roomId, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoom(String date, Long roomId, Long roomId2) {
        return memoryRepository.findByDateAndRoomAndDateAndRoom(date, roomId, roomId2);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndKeyword(String date, Long roomId, Long roomId2, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndKeyword(date, roomId, roomId2, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndEmotion(String date, Long roomId, Long roomId2, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndEmotion(date, roomId, roomId2, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateRange(String date, Long roomId, Long roomId2, String startDate, String endDate) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateRange(date, roomId, roomId2, startDate, endDate);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndKeyword(String date, Long roomId, Long roomId2, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndKeyword(date, roomId, roomId2, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndEmotion(String date, Long roomId, Long roomId2, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndEmotion(date, roomId, roomId2, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoom(String date, Long roomId, Long roomId2, Long roomId3) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoom(date, roomId, roomId2, roomId3);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndKeyword(String date, Long roomId, Long roomId2, Long roomId3, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndKeyword(date, roomId, roomId2, roomId3, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndEmotion(String date, Long roomId, Long roomId2, Long roomId3, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndEmotion(date, roomId, roomId2, roomId3, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateRange(String date, Long roomId, Long roomId2, Long roomId3, String startDate, String endDate) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateRange(date, roomId, roomId2, roomId3, startDate, endDate);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndKeyword(String date, Long roomId, Long roomId2, Long roomId3, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndKeyword(date, roomId, roomId2, roomId3, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndEmotion(String date, Long roomId, Long roomId2, Long roomId3, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndEmotion(date, roomId, roomId2, roomId3, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoom(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoom(date, roomId, roomId2, roomId3, roomId4);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndKeyword(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndKeyword(date, roomId, roomId2, roomId3, roomId4, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndEmotion(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndEmotion(date, roomId, roomId2, roomId3, roomId4, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateRange(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, String startDate, String endDate) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateRange(date, roomId, roomId2, roomId3, roomId4, startDate, endDate);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndKeyword(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, String keyword) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndKeyword(date, roomId, roomId2, roomId3, roomId4, keyword);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndEmotion(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, Emotion emotion) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndEmotion(date, roomId, roomId2, roomId3, roomId4, emotion);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoom(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, Long roomId5) {
        return memoryRepository.findByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoom(date, roomId, roomId2, roomId3, roomId4, roomId5);
    }

    public List<Memory> getMemoriesByDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndDateAndRoomAndKeyword(String date, Long roomId, Long roomId2, Long roomId3, Long roomId4, Long roomId5, String keyword) {
package com.time.PokerFace.memory.service; 