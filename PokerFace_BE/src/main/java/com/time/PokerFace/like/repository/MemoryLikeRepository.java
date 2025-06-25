package com.time.PokerFace.like.repository;

import com.time.PokerFace.like.entity.MemoryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemoryLikeRepository extends JpaRepository<MemoryLike, Long> {
    Optional<MemoryLike> findByMemoryIdAndUserId(Long memoryId, Long userId);
    int countByMemoryId(Long memoryId);
    void deleteByMemoryIdAndUserId(Long memoryId, Long userId);
} 