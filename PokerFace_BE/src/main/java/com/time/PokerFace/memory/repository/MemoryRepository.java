package com.time.PokerFace.memory.repository; 

import com.time.PokerFace.memory.entity.Memory;
import com.time.PokerFace.memory.entity.Emotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    // 기본 조회 메서드들
    Page<Memory> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Memory> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
    Page<Memory> findAllByIdInOrderByCreatedAtDesc(List<Long> ids, Pageable pageable);
    Page<Memory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 감정별 조회 메서드들
    Page<Memory> findByEmotionOrderByCreatedAtDesc(Emotion emotion, Pageable pageable);
    Page<Memory> findByEmotionAndUserIdOrderByCreatedAtDesc(Emotion emotion, Long userId, Pageable pageable);
    List<Memory> findByEmotion(Emotion emotion);
    
    // 랜덤 조회 메서드들
    @Query(value = "SELECT * FROM memories ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Memory> findRandomMemories(@Param("count") int count);
    
    @Query(value = "SELECT * FROM memories WHERE emotion = :emotion ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Memory> findRandomMemoriesByEmotion(@Param("emotion") String emotion, @Param("count") int count);
    
    @Query(value = "SELECT * FROM memories WHERE category = :category ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Memory> findRandomMemoriesByCategory(@Param("category") String category, @Param("count") int count);
    
    @Query(value = "SELECT * FROM memories WHERE emotion = :emotion AND category = :category ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Memory> findRandomMemoriesByEmotionAndCategory(@Param("emotion") String emotion, @Param("category") String category, @Param("count") int count);
} 