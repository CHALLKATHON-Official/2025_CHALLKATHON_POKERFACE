package com.time.PokerFace.social.service;

import com.time.PokerFace.social.dto.MyRoomDecoRequest;
import com.time.PokerFace.social.dto.MyRoomDecoResponse;
import com.time.PokerFace.social.entity.MyRoomDeco;
import com.time.PokerFace.social.repository.MyRoomDecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRoomDecoService {
    private final MyRoomDecoRepository myRoomDecoRepository;

    @Autowired
    public MyRoomDecoService(MyRoomDecoRepository myRoomDecoRepository) {
        this.myRoomDecoRepository = myRoomDecoRepository;
    }

    public MyRoomDecoResponse saveOrUpdateDeco(Long userId, MyRoomDecoRequest request) {
        MyRoomDeco deco = myRoomDecoRepository.findByUserId(userId).orElse(new MyRoomDeco());
        deco.setUserId(userId);
        deco.setBackground(request.getBackground());
        deco.setItems(request.getItems());
        deco.setMusic(request.getMusic());
        myRoomDecoRepository.save(deco);
        return toResponse(deco);
    }

    public MyRoomDecoResponse getDeco(Long userId) {
        MyRoomDeco deco = myRoomDecoRepository.findByUserId(userId).orElse(new MyRoomDeco());
        return toResponse(deco);
    }

    private MyRoomDecoResponse toResponse(MyRoomDeco deco) {
        MyRoomDecoResponse res = new MyRoomDecoResponse();
        res.setBackground(deco.getBackground());
        res.setItems(deco.getItems());
        res.setMusic(deco.getMusic());
        return res;
    }
} 