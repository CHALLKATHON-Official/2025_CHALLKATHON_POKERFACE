package com.time.PokerFace.memory.repository; 

import com.time.PokerFace.memory.entity.Emotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemoryRepository {
    Page<Memory> findByEmotionOrderByCreatedAtDesc(Emotion emotion, Pageable pageable);
    Page<Memory> findByEmotionAndUserIdOrderByCreatedAtDesc(Emotion emotion, Long userId, Pageable pageable);
} 