package com.time.PokerFace.myroom.repository;

import com.time.PokerFace.myroom.entity.MyRoomDeco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyRoomDecoRepository extends JpaRepository<MyRoomDeco, Long> {
    Optional<MyRoomDeco> findByUserId(Long userId);
} 