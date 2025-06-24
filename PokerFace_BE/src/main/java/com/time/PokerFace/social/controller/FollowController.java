package com.time.PokerFace.social.controller;

import com.time.PokerFace.social.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{username}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable String username) {
        followService.follow(userDetails.getUsername(), username);
        return ResponseEntity.ok("팔로우 완료");
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable String username) {
        followService.unfollow(userDetails.getUsername(), username);
        return ResponseEntity.ok("언팔로우 완료");
    }

    @GetMapping("/followers")
    public ResponseEntity<List<String>> getFollowers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(followService.getFollowers(userDetails.getUsername()));
    }

    @GetMapping("/following")
    public ResponseEntity<List<String>> getFollowing(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(followService.getFollowing(userDetails.getUsername()));
    }
}
