package com.time.PokerFace.shop.config;

import com.time.PokerFace.shop.entity.Item;
import com.time.PokerFace.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopDataInitializer {

    private final ItemRepository itemRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            if (itemRepository.count() == 0) {
                initializeSampleItems();
            }
        } catch (Exception e) {
            // 테이블이 아직 없을 때는 무시 (최초 실행 시)
            System.out.println("Item table not ready yet: " + e.getMessage());
        }
    }

    private void initializeSampleItems() {
        // 장식 아이템
        itemRepository.save(Item.builder()
                .name("꽃다발")
                .description("아름다운 꽃다발로 방을 꾸며보세요")
                .price(50)
                .imageUrl("https://via.placeholder.com/100x100?text=Flower")
                .type(Item.ItemType.DECORATION)
                .build());

        itemRepository.save(Item.builder()
                .name("테디베어")
                .description("귀여운 테디베어 인형")
                .price(30)
                .imageUrl("https://via.placeholder.com/100x100?text=Teddy")
                .type(Item.ItemType.DECORATION)
                .build());

        // 배경
        itemRepository.save(Item.builder()
                .name("바다 배경")
                .description("평화로운 바다 풍경")
                .price(100)
                .imageUrl("https://via.placeholder.com/200x100?text=Ocean")
                .type(Item.ItemType.BACKGROUND)
                .build());

        itemRepository.save(Item.builder()
                .name("숲 배경")
                .description("신선한 숲 풍경")
                .price(80)
                .imageUrl("https://via.placeholder.com/200x100?text=Forest")
                .type(Item.ItemType.BACKGROUND)
                .build());

        // 프레임
        itemRepository.save(Item.builder()
                .name("골드 프레임")
                .description("고급스러운 골드 프레임")
                .price(150)
                .imageUrl("https://via.placeholder.com/100x100?text=GoldFrame")
                .type(Item.ItemType.FURNITURE)
                .build());

        itemRepository.save(Item.builder()
                .name("우드 프레임")
                .description("자연스러운 나무 프레임")
                .price(120)
                .imageUrl("https://via.placeholder.com/100x100?text=WoodFrame")
                .type(Item.ItemType.FURNITURE)
                .build());

        // 효과
        itemRepository.save(Item.builder()
                .name("반짝이는 효과")
                .description("반짝이는 특수 효과")
                .price(200)
                .imageUrl("https://via.placeholder.com/100x100?text=Sparkle")
                .type(Item.ItemType.DECORATION)
                .build());

        // 특별 아이템
        itemRepository.save(Item.builder()
                .name("레어 테마")
                .description("매우 희귀한 특별 테마")
                .price(500)
                .imageUrl("https://via.placeholder.com/100x100?text=Rare")
                .type(Item.ItemType.DECORATION)
                .build());
    }
} 