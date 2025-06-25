package com.time.PokerFace.social.repository;

import com.time.PokerFace.social.entity.MyRoomDeco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyRoomDecoRepository extends JpaRepository<MyRoomDeco, Long> {
    Optional<MyRoomDeco> findByUserId(Long userId);
} 