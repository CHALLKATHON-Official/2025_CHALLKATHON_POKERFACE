package com.time.PokerFace.user.service;

import com.time.PokerFace.user.dto.UserSearchResponse;
import com.time.PokerFace.user.dto.UserSearchHistoryResponse;
import com.time.PokerFace.user.dto.UserSearchSuggestResponse;
import com.time.PokerFace.user.entity.UserSearchHistory;
import com.time.PokerFace.user.repository.UserSearchHistoryRepository;
import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserSearchHistoryRepository userSearchHistoryRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserSearchHistoryRepository userSearchHistoryRepository) {
        this.userRepository = userRepository;
        this.userSearchHistoryRepository = userSearchHistoryRepository;
    }

    // 사용자 검색 (닉네임, 이메일, 이름)
    public List<UserSearchResponse> searchUsers(Long userId, String query) {
        List<User> users = userRepository.findTop20ByUsernameContainingIgnoreCaseOrNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, query);
        // 검색 히스토리 저장
        saveSearchHistory(userId, query);
        return users.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    // 검색 히스토리 저장
    public void saveSearchHistory(Long userId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return;
        UserSearchHistory history = new UserSearchHistory();
        history.setUserId(userId);
        history.setSearchKeyword(keyword);
        userSearchHistoryRepository.save(history);
    }

    // 내 검색 히스토리 조회
    public List<UserSearchHistoryResponse> getSearchHistory(Long userId) {
        List<UserSearchHistory> historyList = userSearchHistoryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        List<UserSearchHistoryResponse> responses = new ArrayList<>();
        for (UserSearchHistory h : historyList) {
            UserSearchHistoryResponse dto = new UserSearchHistoryResponse();
            dto.setId(h.getId());
            dto.setKeyword(h.getSearchKeyword());
            dto.setCreatedAt(h.getSearchedAt() != null ? h.getSearchedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            responses.add(dto);
        }
        return responses;
    }

    // 검색 히스토리 삭제
    public void deleteSearchHistory(Long userId, Long historyId) {
        userSearchHistoryRepository.deleteByIdAndUserId(historyId, userId);
    }

    // 추천 검색어 (내 히스토리 + prefix로 시작하는 인기 검색어)
    public UserSearchSuggestResponse suggestKeywords(Long userId, String prefix) {
        List<String> result = new ArrayList<>();
        // 내 히스토리에서 prefix로 시작하는 것
        List<UserSearchHistory> myHistory = userSearchHistoryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        for (UserSearchHistory h : myHistory) {
            if (prefix == null || h.getSearchKeyword().toLowerCase().startsWith(prefix.toLowerCase())) {
                result.add(h.getSearchKeyword());
            }
        }
        // 전체 인기 검색어에서 prefix로 시작하는 것
        List<UserSearchHistory> popular = userSearchHistoryRepository.findTop10BySearchKeywordStartingWithOrderBySearchedAtDesc(prefix == null ? "" : prefix);
        for (UserSearchHistory h : popular) {
            if (!result.contains(h.getSearchKeyword())) {
                result.add(h.getSearchKeyword());
            }
        }
        UserSearchSuggestResponse response = new UserSearchSuggestResponse();
        response.setKeywords(result.stream().distinct().limit(10).collect(Collectors.toList()));
        return response;
    }

    private UserSearchResponse toSearchResponse(User user) {
        UserSearchResponse dto = new UserSearchResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfileImage(user.getProfileImageUrl());
        return dto;
    }
} 