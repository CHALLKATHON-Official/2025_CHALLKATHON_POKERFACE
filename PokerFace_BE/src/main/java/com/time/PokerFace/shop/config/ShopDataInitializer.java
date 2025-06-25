package com.time.PokerFace.shop.config;

import com.time.PokerFace.shop.entity.Item;
import com.time.PokerFace.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ShopDataInitializer implements CommandLineRunner {
    
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        // 샘플 아이템 데이터가 없으면 추가
        if (itemRepository.count() == 0) {
            initializeSampleItems();
        }
    }

    private void initializeSampleItems() {
        // 장식 아이템
        Item decoration1 = new Item();
        decoration1.setName("꽃다발");
        decoration1.setDescription("아름다운 꽃다발로 방을 꾸며보세요");
        decoration1.setPrice(50);
        decoration1.setImageUrl("https://via.placeholder.com/100x100?text=Flower");
        decoration1.setCategory(Item.ItemCategory.DECORATION);
        itemRepository.save(decoration1);

        Item decoration2 = new Item();
        decoration2.setName("테디베어");
        decoration2.setDescription("귀여운 테디베어 인형");
        decoration2.setPrice(30);
        decoration2.setImageUrl("https://via.placeholder.com/100x100?text=Teddy");
        decoration2.setCategory(Item.ItemCategory.DECORATION);
        itemRepository.save(decoration2);

        // 배경
        Item background1 = new Item();
        background1.setName("바다 배경");
        background1.setDescription("평화로운 바다 풍경");
        background1.setPrice(100);
        background1.setImageUrl("https://via.placeholder.com/200x100?text=Ocean");
        background1.setCategory(Item.ItemCategory.BACKGROUND);
        itemRepository.save(background1);

        Item background2 = new Item();
        background2.setName("숲 배경");
        background2.setDescription("신선한 숲 풍경");
        background2.setPrice(80);
        background2.setImageUrl("https://via.placeholder.com/200x100?text=Forest");
        background2.setCategory(Item.ItemCategory.BACKGROUND);
        itemRepository.save(background2);

        // 프레임
        Item frame1 = new Item();
        frame1.setName("골드 프레임");
        frame1.setDescription("고급스러운 골드 프레임");
        frame1.setPrice(150);
        frame1.setImageUrl("https://via.placeholder.com/100x100?text=GoldFrame");
        frame1.setCategory(Item.ItemCategory.FRAME);
        itemRepository.save(frame1);

        Item frame2 = new Item();
        frame2.setName("우드 프레임");
        frame2.setDescription("자연스러운 나무 프레임");
        frame2.setPrice(120);
        frame2.setImageUrl("https://via.placeholder.com/100x100?text=WoodFrame");
        frame2.setCategory(Item.ItemCategory.FRAME);
        itemRepository.save(frame2);

        // 효과
        Item effect1 = new Item();
        effect1.setName("반짝이는 효과");
        effect1.setDescription("반짝이는 특수 효과");
        effect1.setPrice(200);
        effect1.setImageUrl("https://via.placeholder.com/100x100?text=Sparkle");
        effect1.setCategory(Item.ItemCategory.EFFECT);
        itemRepository.save(effect1);

        // 특별 아이템
        Item special1 = new Item();
        special1.setName("레어 테마");
        special1.setDescription("매우 희귀한 특별 테마");
        special1.setPrice(500);
        special1.setImageUrl("https://via.placeholder.com/100x100?text=Rare");
        special1.setCategory(Item.ItemCategory.SPECIAL);
        itemRepository.save(special1);
    }
} 