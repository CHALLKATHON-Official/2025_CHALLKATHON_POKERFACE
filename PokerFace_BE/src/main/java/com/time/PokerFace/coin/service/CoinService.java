package com.time.PokerFace.coin.service;

import com.time.PokerFace.coin.dto.CoinBalanceResponse;
import com.time.PokerFace.coin.dto.CoinHistoryResponse;
import com.time.PokerFace.coin.dto.CoinEarnRequest;
import com.time.PokerFace.coin.entity.Coin;
import com.time.PokerFace.coin.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {
    private final CoinRepository coinRepository;

    @Autowired
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public void earnCoins(Long userId, int amount, String description) {
        Coin coin = new Coin();
        coin.setUserId(userId);
        coin.setAmount(amount);
        coin.setType(Coin.CoinType.EARN);
        coin.setDescription(description);
        coinRepository.save(coin);
    }

    public void spendCoins(Long userId, int amount, String description) {
        // 잔액 확인
        int currentBalance = getBalance(userId);
        if (currentBalance < amount) {
            throw new RuntimeException("Insufficient coins");
        }

        Coin coin = new Coin();
        coin.setUserId(userId);
        coin.setAmount(-amount); // 음수로 저장
        coin.setType(Coin.CoinType.SPEND);
        coin.setDescription(description);
        coinRepository.save(coin);
    }

    public int getBalance(Long userId) {
        return coinRepository.sumAmountByUserId(userId);
    }

    public CoinBalanceResponse getBalanceResponse(Long userId) {
        CoinBalanceResponse response = new CoinBalanceResponse();
        response.setBalance(getBalance(userId));
        return response;
    }

    public CoinHistoryResponse getHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Coin> coinPage = coinRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        List<CoinHistoryResponse.CoinHistoryItem> items = new ArrayList<>();
        for (Coin coin : coinPage.getContent()) {
            CoinHistoryResponse.CoinHistoryItem item = new CoinHistoryResponse.CoinHistoryItem();
            item.setId(coin.getId());
            item.setAmount(coin.getAmount());
            item.setType(coin.getType().name());
            item.setDescription(coin.getDescription());
            item.setCreatedAt(coin.getCreatedAt() != null ? 
                coin.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            items.add(item);
        }

        CoinHistoryResponse response = new CoinHistoryResponse();
        response.setHistory(items);
        response.setTotalPages(coinPage.getTotalPages());
        response.setTotalElements(coinPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        return response;
    }

    // 자동 코인 적립 메서드들
    public void earnCoinsForMemoryUpload(Long userId) {
        earnCoins(userId, 10, "메모리 업로드");
    }

    public void earnCoinsForLike(Long userId) {
        earnCoins(userId, 1, "좋아요 받음");
    }

    public void earnCoinsForComment(Long userId) {
        earnCoins(userId, 2, "댓글 받음");
    }

    public void earnCoinsForFollow(Long userId) {
        earnCoins(userId, 5, "새 팔로워");
    }
} 