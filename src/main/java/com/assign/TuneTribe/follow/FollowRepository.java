package com.assign.TuneTribe.follow;

import com.assign.TuneTribe.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shauna
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    long countByFollowing(User following);

    long countByFollower(User follower);

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);
}
