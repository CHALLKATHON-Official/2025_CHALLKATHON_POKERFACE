package com.time.PokerFace.social.repository;

import com.time.PokerFace.auth.entity.User;
import com.time.PokerFace.social.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollower(User user);
    List<Follow> findByFollowing(User user);

    boolean existsByFollowerAndFollowing(User follower, User following);
}
