package com.time.PokerFace.comment.service;

import com.time.PokerFace.comment.dto.CommentRequest;
import com.time.PokerFace.comment.dto.CommentResponse;
import com.time.PokerFace.comment.entity.Comment;
import com.time.PokerFace.comment.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
        return commentRepository.save(comment);
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