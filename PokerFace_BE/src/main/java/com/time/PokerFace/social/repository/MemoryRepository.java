package com.time.PokerFace.social.repository;

import com.time.PokerFace.social.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    // 추후 커스텀 쿼리 메소드 추가 가능
} 