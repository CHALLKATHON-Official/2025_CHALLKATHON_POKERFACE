package com.time.PokerFace.book.repository;

import com.time.PokerFace.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(Long userId);
    List<Book> findByUserIdOrderByCreatedAtDesc(Long userId);
} 