package com.time.PokerFace.social.entity;

import javax.persistence.*;

@Entity
@Table(name = "myroom_deco")
public class MyRoomDeco {
    @Id
    private Long userId;

    private String background;

    @Column(columnDefinition = "TEXT")
    private String items; // JSON 문자열로 저장

    private String music;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBackground() { return background; }
    public void setBackground(String background) { this.background = background; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public String getMusic() { return music; }
    public void setMusic(String music) { this.music = music; }
} 