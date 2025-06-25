package com.time.PokerFace.memory.dto;

import java.util.List;

public class MemoryFilterRequest {
    private String emotion;
    private String category;
    private List<String> tags;
    private int page = 0;
    private int size = 10;

    // Getters and Setters
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
} 