package com.time.PokerFace.social.entity;

import javax.persistence.*;

@Entity
@Table(name = "memory_bookmarks", uniqueConstraints = {@UniqueConstraint(columnNames = {"memory_id", "user_id"})})
public class MemoryBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "memory_id", nullable = false)
    private Long memoryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemoryId() { return memoryId; }
    public void setMemoryId(Long memoryId) { this.memoryId = memoryId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
} 