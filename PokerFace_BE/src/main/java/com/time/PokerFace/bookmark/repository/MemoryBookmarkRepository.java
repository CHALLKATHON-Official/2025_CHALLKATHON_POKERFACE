package com.time.PokerFace.bookmark.repository;

import com.time.PokerFace.bookmark.entity.MemoryBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryBookmarkRepository extends JpaRepository<MemoryBookmark, Long> {
    List<MemoryBookmark> findByUserId(Long userId);
    Optional<MemoryBookmark> findByMemoryIdAndUserId(Long memoryId, Long userId);
    void deleteByMemoryIdAndUserId(Long memoryId, Long userId);
} 