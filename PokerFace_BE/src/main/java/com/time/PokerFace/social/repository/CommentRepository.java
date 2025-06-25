package com.time.PokerFace.social.repository;

import com.time.PokerFace.social.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMemoryIdOrderByCreatedAtAsc(Long memoryId);
    void deleteByIdAndUserId(Long id, Long userId);
} 