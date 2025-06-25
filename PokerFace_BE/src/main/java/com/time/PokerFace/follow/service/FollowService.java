package com.time.PokerFace.follow.service;

import com.time.PokerFace.follow.entity.Follow;
import com.time.PokerFace.follow.repository.FollowRepository;

import java.util.List;

public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public List<Follow> getAllFollows() {
        return followRepository.findAll();
    }

    public Follow getFollowById(Long id) {
        return followRepository.findById(id).orElse(null);
    }

    public Follow saveFollow(Follow follow) {
        return followRepository.save(follow);
    }

    public void deleteFollow(Long id) {
        followRepository.deleteById(id);
    }
} 