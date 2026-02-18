package com.assign.TuneTribe.follow;

import com.assign.TuneTribe.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    public long countFollowers(User user) {
        if (user == null) {
            return 0;
        }
        return followRepository.countByFollowing(user);
    }

    public long countFollowing(User user) {
        if (user == null) {
            return 0;
        }
        return followRepository.countByFollower(user);
    }

    public boolean isFollowing(User follower, User following) {
        if (follower == null || following == null) {
            return false;
        }
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public void follow(User follower, User following) {
        if (follower == null || following == null) {
            return;
        }
        if (follower.getId() == following.getId()) {
            return;
        }
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            return;
        }
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(User follower, User following) {
        if (follower == null || following == null) {
            return;
        }
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }
}
