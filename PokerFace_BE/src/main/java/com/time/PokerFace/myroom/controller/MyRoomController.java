package com.time.PokerFace.myroom.controller;

import com.time.PokerFace.myroom.dto.MyRoomDecoRequest;
import com.time.PokerFace.myroom.dto.MyRoomDecoResponse;
import com.time.PokerFace.myroom.service.MyRoomDecoService;
import com.time.PokerFace.memory.service.MemoryService;
import com.time.PokerFace.myroom.dto.MyRoomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myroom")
public class MyRoomController {
    private final MyRoomDecoService myRoomDecoService;
    private final MemoryService memoryService;

    public MyRoomController(MyRoomDecoService myRoomDecoService, MemoryService memoryService) {
        this.myRoomDecoService = myRoomDecoService;
        this.memoryService = memoryService;
    }

    @GetMapping("/deco")
    public ResponseEntity<MyRoomDecoResponse> getMyRoomDeco() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MyRoomDecoResponse response = myRoomDecoService.getMyRoomDeco(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deco")
    public ResponseEntity<MyRoomDecoResponse> updateMyRoomDeco(@RequestBody MyRoomDecoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MyRoomDecoResponse response = myRoomDecoService.createOrUpdateMyRoomDeco(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deco")
    public ResponseEntity<Void> deleteMyRoomDeco() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        myRoomDecoService.deleteMyRoomDeco(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/memories")
    public ResponseEntity<MyRoomResponse> getMyMemories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MyRoomResponse response = memoryService.getMyMemories(userId, page, size);
        return ResponseEntity.ok(response);
    }
} 