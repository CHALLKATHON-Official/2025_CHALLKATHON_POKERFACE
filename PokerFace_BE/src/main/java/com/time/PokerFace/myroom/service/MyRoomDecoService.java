package com.time.PokerFace.myroom.service;

import com.time.PokerFace.myroom.dto.MyRoomDecoRequest;
import com.time.PokerFace.myroom.dto.MyRoomDecoResponse;
import com.time.PokerFace.myroom.entity.MyRoomDeco;
import com.time.PokerFace.myroom.repository.MyRoomDecoRepository;

import java.util.List;
import java.util.Optional;

public class MyRoomDecoService {

    private final MyRoomDecoRepository myRoomDecoRepository;

    public MyRoomDecoService(MyRoomDecoRepository myRoomDecoRepository) {
        this.myRoomDecoRepository = myRoomDecoRepository;
    }

    public List<MyRoomDeco> getAllMyRoomDecos() {
        return myRoomDecoRepository.findAll();
    }

    public Optional<MyRoomDeco> getMyRoomDecoById(Long id) {
        return myRoomDecoRepository.findById(id);
    }

    public MyRoomDeco createMyRoomDeco(MyRoomDecoDecoRequest request) {
        MyRoomDeco myRoomDeco = new MyRoomDeco();
        myRoomDeco.setName(request.getName());
        myRoomDeco.setDescription(request.getDescription());
        return myRoomDecoRepository.save(myRoomDeco);
    }

    public MyRoomDeco updateMyRoomDeco(Long id, MyRoomDecoDecoRequest request) {
        Optional<MyRoomDeco> optionalMyRoomDeco = myRoomDecoRepository.findById(id);
        if (optionalMyRoomDeco.isPresent()) {
            MyRoomDeco myRoomDeco = optionalMyRoomDeco.get();
            myRoomDeco.setName(request.getName());
            myRoomDeco.setDescription(request.getDescription());
            return myRoomDecoRepository.save(myRoomDeco);
        } else {
            throw new RuntimeException("MyRoomDeco not found");
        }
    }

    public void deleteMyRoomDeco(Long id) {
        myRoomDecoRepository.deleteById(id);
    }
} 