package com.time.PokerFace.shop.service;

import com.time.PokerFace.shop.dto.ItemResponse;
import com.time.PokerFace.shop.dto.MyItemResponse;
import com.time.PokerFace.shop.dto.PurchaseRequest;
import com.time.PokerFace.shop.entity.Item;
import com.time.PokerFace.shop.entity.UserItem;
import com.time.PokerFace.shop.repository.ItemRepository;
import com.time.PokerFace.shop.repository.UserItemRepository;
import com.time.PokerFace.coin.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final CoinService coinService;

    @Autowired
    public ShopService(ItemRepository itemRepository, UserItemRepository userItemRepository, CoinService coinService) {
        this.itemRepository = itemRepository;
        this.userItemRepository = userItemRepository;
        this.coinService = coinService;
    }

    public List<ItemResponse> getItems() {
        List<Item> items = itemRepository.findByIsActiveTrue();
        List<ItemResponse> responses = new ArrayList<>();
        for (Item item : items) {
            responses.add(toItemResponse(item));
        }
        return responses;
    }

    public void purchaseItem(Long userId, PurchaseRequest request) {
        Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
        if (!itemOpt.isPresent()) {
            throw new RuntimeException("Item not found");
        }
        Item item = itemOpt.get();
        
        // if (!item.isActive()) {
        //     throw new RuntimeException("Item is not available for purchase");
        // }

        // 코인 차감
        coinService.spendCoins(userId, item.getPrice(), "아이템 구매: " + item.getName());

        // 사용자 아이템에 추가
        UserItem userItem = new UserItem();
        userItem.setUserId(userId);
        userItem.setItemId(item.getId());
        userItemRepository.save(userItem);
    }

    public MyItemResponse getMyItems(Long userId) {
        List<UserItem> userItems = userItemRepository.findByUserId(userId);
        List<MyItemResponse.MyItemInfo> items = new ArrayList<>();
        
        for (UserItem userItem : userItems) {
            Optional<Item> itemOpt = itemRepository.findById(userItem.getItemId());
            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                MyItemResponse.MyItemInfo itemInfo = new MyItemResponse.MyItemInfo();
                itemInfo.setId(item.getId());
                itemInfo.setName(item.getName());
                itemInfo.setDescription(item.getDescription());
                itemInfo.setImageUrl(item.getImageUrl());
                itemInfo.setType(item.getType().name());
                itemInfo.setPurchasedAt(userItem.getPurchasedAt() != null ? 
                    userItem.getPurchasedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
                items.add(itemInfo);
            }
        }
        
        MyItemResponse response = new MyItemResponse();
        response.setItems(items);
        return response;
    }

    private ItemResponse toItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setImageUrl(item.getImageUrl());
        response.setType(item.getType().name());
        return response;
    }
} 