package com.time.PokerFace.comment.dto;

public class CommentRequest {
    private String content;
    private Long memoryId;
    private Long userId;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getMemoryId() { return memoryId; }
    public void setMemoryId(Long memoryId) { this.memoryId = memoryId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
} 