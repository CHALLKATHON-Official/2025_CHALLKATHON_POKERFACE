package com.time.PokerFace.memory.dto;

public class MemoryRecommendRequest {
    private String emotion;
    private String type = "popular"; // popular, recent, random
    private int size = 5;

    // Getters and Setters
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
} 