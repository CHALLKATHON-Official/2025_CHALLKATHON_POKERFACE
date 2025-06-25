package com.time.PokerFace.user.dto;

import java.util.List;

public class UserSearchSuggestResponse {
    private List<String> keywords;

    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
} 