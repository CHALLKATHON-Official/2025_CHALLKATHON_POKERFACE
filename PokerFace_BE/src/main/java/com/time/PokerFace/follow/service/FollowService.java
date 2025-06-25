package com.time.PokerFace.follow.service;

import com.time.PokerFace.follow.entity.Follow;
import com.time.PokerFace.follow.repository.FollowRepository;
import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.notification.service.NotificationService;
import com.time.PokerFace.notification.entity.Notification;
import com.time.PokerFace.coin.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final NotificationService notificationService;
    private final CoinService coinService;

    @Autowired
    public FollowService(FollowRepository followRepository, NotificationService notificationService, CoinService coinService) {
        this.followRepository = followRepository;
        this.notificationService = notificationService;
        this.coinService = coinService;
    }

    public List<Follow> getAllFollows() {
        return followRepository.findAll();
    }

    public Follow getFollowById(Long id) {
        return followRepository.findById(id).orElse(null);
    }

    public Follow saveFollow(Follow follow) {
        Follow savedFollow = followRepository.save(follow);
        
        // 팔로우 알림 생성
        notificationService.createNotification(
            follow.getFollowing().getId(),
            Notification.NotificationType.FOLLOW,
            "새로운 팔로워",
            "누군가 당신을 팔로우하기 시작했습니다.",
            follow.getFollower().getId()
        );
        
        // 팔로우 받은 사용자에게 코인 적립
        coinService.earnCoinsForFollow(follow.getFollowing().getId());
        
        return savedFollow;
    }

    public void deleteFollow(Long id) {
        followRepository.deleteById(id);
    }

    public boolean isFollowing(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public List<Follow> getFollowers(User user) {
        return followRepository.findByFollowing(user);
    }

    public List<Follow> getFollowing(User user) {
        return followRepository.findByFollower(user);
    }
} 