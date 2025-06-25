package com.time.PokerFace.social.repository;

import com.time.PokerFace.social.entity.Memory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    // 추후 커스텀 쿼리 메소드 추가 가능
    Page<Memory> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Memory> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
    Page<Memory> findAllByIdInOrderByCreatedAtDesc(List<Long> ids, Pageable pageable);
    Page<Memory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
} 