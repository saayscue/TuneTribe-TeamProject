package com.assign.TuneTribe.post;

import com.assign.TuneTribe.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shauna
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByUser_BannedFalseOrderByCreatedAtDesc();

    List<Post> findByUserOrderByCreatedAtDesc(User user);

    List<Post> findTop10ByOrderByCreatedAtDesc();

    List<Post> findTop10ByUser_BannedFalseOrderByCreatedAtDesc();

    long countByUser(User user);
}
