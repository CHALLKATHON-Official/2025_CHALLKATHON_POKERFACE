package com.time.PokerFace.memory.dto;

public class RandomMemoryRequest {
    private String emotion;
    private String category;
    private int count = 1; // 기본값 1개

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
} 