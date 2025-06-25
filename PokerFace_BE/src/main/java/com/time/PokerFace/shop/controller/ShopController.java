package com.time.PokerFace.shop.controller;

import com.time.PokerFace.shop.dto.ItemResponse;
import com.time.PokerFace.shop.dto.MyItemResponse;
import com.time.PokerFace.shop.dto.PurchaseRequest;
import com.time.PokerFace.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getItems() {
        List<ItemResponse> items = shopService.getItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseItem(@RequestBody PurchaseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        shopService.purchaseItem(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-items")
    public ResponseEntity<MyItemResponse> getMyItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        MyItemResponse response = shopService.getMyItems(userId);
        return ResponseEntity.ok(response);
    }
} 