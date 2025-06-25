package com.time.PokerFace.shop.dto;

import java.util.List;

public class MyItemResponse {
    private List<MyItemInfo> items;

    public static class MyItemInfo {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private String category;
        private String purchasedAt;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getPurchasedAt() { return purchasedAt; }
        public void setPurchasedAt(String purchasedAt) { this.purchasedAt = purchasedAt; }
    }

    // Getters and Setters
    public List<MyItemInfo> getItems() { return items; }
    public void setItems(List<MyItemInfo> items) { this.items = items; }
} 