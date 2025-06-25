package com.time.PokerFace.social.service;

import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.auth.repository.UserRepository;
import com.time.PokerFace.social.entity.Follow;
import com.time.PokerFace.social.repository.FollowRepository;
import com.time.PokerFace.notification.service.NotificationService;
import com.time.PokerFace.notification.entity.Notification;
import com.time.PokerFace.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final CoinService coinService;

    public void follow(String followerUsername, String targetUsername) {
        if (followerUsername.equals(targetUsername)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }

        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저 없음"));
        User following = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new IllegalArgumentException("대상 유저 없음"));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우한 유저입니다.");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(follow);

        // 팔로우 알림 생성
        notificationService.createNotification(
            following.getId(),
            Notification.NotificationType.FOLLOW,
            "새로운 팔로워",
            followerUsername + "님이 당신을 팔로우하기 시작했습니다.",
            follower.getId()
        );
        
        // 팔로우 받은 사용자에게 코인 적립
        coinService.earnCoinsForFollow(following.getId());
    }

    public void unfollow(String followerUsername, String targetUsername) {
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저 없음"));
        User following = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new IllegalArgumentException("대상 유저 없음"));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));
        followRepository.delete(follow);
    }

    public List<String> getFollowers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return followRepository.findByFollowing(user).stream()
                .map(f -> f.getFollower().getUsername())
                .collect(Collectors.toList());
    }

    public List<String> getFollowing(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return followRepository.findByFollower(user).stream()
                .map(f -> f.getFollowing().getUsername())
                .collect(Collectors.toList());
    }
}
