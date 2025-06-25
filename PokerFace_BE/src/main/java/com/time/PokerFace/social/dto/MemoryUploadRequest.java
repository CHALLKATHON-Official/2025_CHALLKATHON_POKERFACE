package com.time.PokerFace.social.dto;

import org.springframework.web.multipart.MultipartFile;

public class MemoryUploadRequest {
    private String content;
    private String emotion;
    private MultipartFile image;

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
} 