package com.time.PokerFace.shop.repository;

import com.time.PokerFace.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByIsActiveTrue();
} 