package com.time.PokerFace.social.dto;

import java.util.List;

public class MemoryDetailResponse {
    private Long id;
    private String content;
    private String emotion;
    private String imageUrl;
    private String createdAt;
    private Long userId;
    private int likes; // 공감수(임시)
    private List<String> comments; // 댓글(임시)

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }
} 