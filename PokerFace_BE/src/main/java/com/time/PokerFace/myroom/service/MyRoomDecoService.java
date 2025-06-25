package com.time.PokerFace.myroom.service;

import com.time.PokerFace.myroom.dto.MyRoomDecoRequest;
import com.time.PokerFace.myroom.dto.MyRoomDecoResponse;
import com.time.PokerFace.myroom.entity.MyRoomDeco;
import com.time.PokerFace.myroom.repository.MyRoomDecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyRoomDecoService {

    private final MyRoomDecoRepository myRoomDecoRepository;

    @Autowired
    public MyRoomDecoService(MyRoomDecoRepository myRoomDecoRepository) {
        this.myRoomDecoRepository = myRoomDecoRepository;
    }

    public List<MyRoomDeco> getAllMyRoomDecos() {
        return myRoomDecoRepository.findAll();
    }

    public Optional<MyRoomDeco> getMyRoomDecoById(Long id) {
        return myRoomDecoRepository.findById(id);
    }

    public MyRoomDecoResponse getMyRoomDeco(Long userId) {
        Optional<MyRoomDeco> optional = myRoomDecoRepository.findByUserId(userId);
        if (optional.isPresent()) {
            MyRoomDeco deco = optional.get();
            MyRoomDecoResponse response = new MyRoomDecoResponse();
            response.setBackground(deco.getBackground());
            response.setItems(deco.getItems());
            response.setMusic(deco.getMusic());
            return response;
        }
        return null;
    }

    public MyRoomDecoResponse createOrUpdateMyRoomDeco(Long userId, MyRoomDecoRequest request) {
        Optional<MyRoomDeco> optional = myRoomDecoRepository.findByUserId(userId);
        MyRoomDeco myRoomDeco;
        
        if (optional.isPresent()) {
            myRoomDeco = optional.get();
        } else {
            myRoomDeco = new MyRoomDeco();
            myRoomDeco.setUserId(userId);
        }
        
        if (request.getBackground() != null) {
            myRoomDeco.setBackground(request.getBackground());
        }
        if (request.getItems() != null) {
            myRoomDeco.setItems(request.getItems());
        }
        if (request.getMusic() != null) {
            myRoomDeco.setMusic(request.getMusic());
        }
        
        MyRoomDeco saved = myRoomDecoRepository.save(myRoomDeco);
        
        MyRoomDecoResponse response = new MyRoomDecoResponse();
        response.setBackground(saved.getBackground());
        response.setItems(saved.getItems());
        response.setMusic(saved.getMusic());
        return response;
    }

    public void deleteMyRoomDeco(Long userId) {
        Optional<MyRoomDeco> optional = myRoomDecoRepository.findByUserId(userId);
        if (optional.isPresent()) {
            myRoomDecoRepository.delete(optional.get());
        }
    }
} 