package com.time.PokerFace.social.controller;

import com.time.PokerFace.social.dto.MyRoomDecoRequest;
import com.time.PokerFace.social.dto.MyRoomDecoResponse;
import com.time.PokerFace.social.service.MyRoomDecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myroom/deco")
public class MyRoomController {
    private final MyRoomDecoService myRoomDecoService;

    @Autowired
    public MyRoomController(MyRoomDecoService myRoomDecoService) {
        this.myRoomDecoService = myRoomDecoService;
    }

    @PostMapping
    public ResponseEntity<MyRoomDecoResponse> saveOrUpdateDeco(@RequestBody MyRoomDecoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        MyRoomDecoResponse response = myRoomDecoService.saveOrUpdateDeco(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<MyRoomDecoResponse> getDeco() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        MyRoomDecoResponse response = myRoomDecoService.getDeco(userId);
        return ResponseEntity.ok(response);
    }
} 