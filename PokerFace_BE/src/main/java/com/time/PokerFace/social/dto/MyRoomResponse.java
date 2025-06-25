package com.time.PokerFace.social.dto;

import java.util.List;

public class MyRoomResponse {
    private List<MemoryListItem> memories;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public List<MemoryListItem> getMemories() { return memories; }
    public void setMemories(List<MemoryListItem> memories) { this.memories = memories; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
} 