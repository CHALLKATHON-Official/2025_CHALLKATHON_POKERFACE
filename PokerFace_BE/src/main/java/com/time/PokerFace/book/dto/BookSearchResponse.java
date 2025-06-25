package com.time.PokerFace.book.dto;

import java.util.List;

public class BookSearchResponse {
    private List<BookInfo> books;
    private int totalItems;

    public static class BookInfo {
        private String title;
        private String author;
        private String imageUrl;
        private String isbn;
        private String description;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Getters and Setters
    public List<BookInfo> getBooks() { return books; }
    public void setBooks(List<BookInfo> books) { this.books = books; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
} 