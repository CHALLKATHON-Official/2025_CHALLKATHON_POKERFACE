package com.time.PokerFace.coin.repository;

import com.time.PokerFace.coin.entity.Coin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Page<Coin> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Coin c WHERE c.userId = :userId")
    int sumAmountByUserId(@Param("userId") Long userId);
} 