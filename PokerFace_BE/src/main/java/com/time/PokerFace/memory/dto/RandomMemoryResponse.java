package com.time.PokerFace.memory.dto;

import java.util.List;

public class RandomMemoryResponse {
    private List<RandomMemoryItem> memories;
    private int totalCount;

    public static class RandomMemoryItem {
        private Long id;
        private String content;
        private String emotion;
        private String imageUrl;
        private Long userId;
        private String createdAt;
        private int likeCount;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getEmotion() { return emotion; }
        public void setEmotion(String emotion) { this.emotion = emotion; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public int getLikeCount() { return likeCount; }
        public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    }

    // Getters and Setters
    public List<RandomMemoryItem> getMemories() { return memories; }
    public void setMemories(List<RandomMemoryItem> memories) { this.memories = memories; }
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
} 