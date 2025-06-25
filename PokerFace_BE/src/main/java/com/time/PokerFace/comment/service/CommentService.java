package com.time.PokerFace.comment.service;

import com.time.PokerFace.comment.dto.CommentRequest;
import com.time.PokerFace.comment.dto.CommentResponse;
import com.time.PokerFace.comment.entity.Comment;
import com.time.PokerFace.comment.repository.CommentRepository;
import com.time.PokerFace.notification.service.NotificationService;
import com.time.PokerFace.notification.entity.Notification;
import com.time.PokerFace.memory.repository.MemoryRepository;
import com.time.PokerFace.memory.entity.Memory;
import com.time.PokerFace.coin.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final MemoryRepository memoryRepository;
    private final CoinService coinService;

    @Autowired
    public CommentService(CommentRepository commentRepository, NotificationService notificationService, MemoryRepository memoryRepository, CoinService coinService) {
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
        this.memoryRepository = memoryRepository;
        this.coinService = coinService;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setMemoryId(commentRequest.getMemoryId());
        comment.setUserId(commentRequest.getUserId());
        Comment savedComment = commentRepository.save(comment);

        // 댓글 알림 생성
        Optional<Memory> memoryOpt = memoryRepository.findById(commentRequest.getMemoryId());
        if (memoryOpt.isPresent()) {
            Memory memory = memoryOpt.get();
            // 자신의 메모리에 댓글을 단 경우는 알림 생성하지 않음
            if (!memory.getUserId().equals(commentRequest.getUserId())) {
                notificationService.createNotification(
                    memory.getUserId(),
                    Notification.NotificationType.COMMENT,
                    "새로운 댓글",
                    "누군가 당신의 메모리에 댓글을 남겼습니다.",
                    commentRequest.getMemoryId()
                );
                
                // 댓글 받은 사용자에게 코인 적립
                coinService.earnCoinsForComment(memory.getUserId());
            }
        }

        return savedComment;
    }

    public Comment updateComment(Long id, CommentRequest commentRequest) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(commentRequest.getContent());
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
} 