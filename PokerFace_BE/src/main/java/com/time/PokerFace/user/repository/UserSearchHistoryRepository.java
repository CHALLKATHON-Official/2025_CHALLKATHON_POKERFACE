package com.time.PokerFace.user.repository;

import com.time.PokerFace.user.entity.UserSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSearchHistoryRepository extends JpaRepository<UserSearchHistory, Long> {
    List<UserSearchHistory> findByUserIdOrderBySearchedAtDesc(Long userId);
    List<UserSearchHistory> findTop10ByUserIdOrderBySearchedAtDesc(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
    List<UserSearchHistory> findTop10BySearchKeywordStartingWithOrderBySearchedAtDesc(String prefix);
    List<UserSearchHistory> findTop10ByOrderBySearchKeywordAsc();
} 