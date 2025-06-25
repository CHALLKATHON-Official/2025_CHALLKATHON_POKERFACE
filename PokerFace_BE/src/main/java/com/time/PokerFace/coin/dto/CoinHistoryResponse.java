package com.time.PokerFace.coin.dto;

import java.util.List;

public class CoinHistoryResponse {
    private List<CoinHistoryItem> history;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public static class CoinHistoryItem {
        private Long id;
        private int amount;
        private String type;
        private String description;
        private String createdAt;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    // Getters and Setters
    public List<CoinHistoryItem> getHistory() { return history; }
    public void setHistory(List<CoinHistoryItem> history) { this.history = history; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
} 