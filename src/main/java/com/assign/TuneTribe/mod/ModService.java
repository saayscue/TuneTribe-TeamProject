
package com.assign.TuneTribe.mod;

import com.assign.TuneTribe.follow.Follow;
import com.assign.TuneTribe.follow.FollowRepository;
import com.assign.TuneTribe.post.Post;
import com.assign.TuneTribe.post.PostRepository;
import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModService {

    @Autowired
    ModRepository repo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FollowRepository followRepository;

    public List<Mod> getMods() {
        List<Mod> allMods = repo.findAll();
        return allMods;
    }

    public Mod getMod(long id) {
        return repo.getReferenceById(id);
    }

    public void deleteMod(long id) {
        repo.deleteById(id);
    }

    public List<User> getUsersForModeration(String roleFilter, String statusFilter) {
        return userRepository.findAll().stream()
                .filter(user -> !"Admin".equalsIgnoreCase(user.getRole()))
                .filter(user -> !"Mod".equalsIgnoreCase(user.getRole()))
                .filter(user -> {
                    if (roleFilter == null || "all".equalsIgnoreCase(roleFilter)) {
                        return true;
                    }
                    return roleFilter.equalsIgnoreCase(user.getRole());
                })
                .filter(user -> {
                    if (statusFilter == null || "all".equalsIgnoreCase(statusFilter)) {
                        return true;
                    }
                    if ("active".equalsIgnoreCase(statusFilter)) {
                        return !Boolean.TRUE.equals(user.isBanned());
                    }
                    if ("banned".equalsIgnoreCase(statusFilter)) {
                        return Boolean.TRUE.equals(user.isBanned());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<Post> getPostsForModeration(String roleFilter, String statusFilter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth != null ? auth.getName() : null;
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(post -> {
                    if (currentUserName == null || post.getUser() == null) {
                        return true;
                    }
                    return !currentUserName.equalsIgnoreCase(post.getUser().getUserName());
                })
                .filter(post -> {
                    if (roleFilter == null || "all".equalsIgnoreCase(roleFilter)) {
                        return true;
                    }
                    if (post.getUser() == null || post.getUser().getRole() == null) {
                        return false;
                    }
                    return roleFilter.equalsIgnoreCase(post.getUser().getRole());
                })
                .filter(post -> {
                    if (statusFilter == null || "all".equalsIgnoreCase(statusFilter)) {
                        return true;
                    }
                    if (post.getUser() == null) {
                        return false;
                    }
                    if ("active".equalsIgnoreCase(statusFilter)) {
                        return !Boolean.TRUE.equals(post.getUser().isBanned());
                    }
                    if ("banned".equalsIgnoreCase(statusFilter)) {
                        return Boolean.TRUE.equals(post.getUser().isBanned());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<Follow> getFollowsForModeration(String roleFilter, String statusFilter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth != null ? auth.getName() : null;
        return followRepository.findAll().stream()
                .filter(follow -> {
                    if (currentUserName == null) {
                        return true;
                    }
                    if (follow.getFollower() != null
                            && currentUserName.equalsIgnoreCase(follow.getFollower().getUserName())) {
                        return false;
                    }
                    if (follow.getFollowing() != null
                            && currentUserName.equalsIgnoreCase(follow.getFollowing().getUserName())) {
                        return false;
                    }
                    return true;
                })
                .filter(follow -> {
                    if (roleFilter == null || "all".equalsIgnoreCase(roleFilter)) {
                        return true;
                    }
                    if (follow.getFollower() == null || follow.getFollower().getRole() == null) {
                        return false;
                    }
                    return roleFilter.equalsIgnoreCase(follow.getFollower().getRole());
                })
                .filter(follow -> {
                    if (statusFilter == null || "all".equalsIgnoreCase(statusFilter)) {
                        return true;
                    }
                    if (follow.getFollower() == null) {
                        return false;
                    }
                    if ("active".equalsIgnoreCase(statusFilter)) {
                        return !Boolean.TRUE.equals(follow.getFollower().isBanned());
                    }
                    if ("banned".equalsIgnoreCase(statusFilter)) {
                        return Boolean.TRUE.equals(follow.getFollower().isBanned());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void toggleUserBan(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setBanned(!user.isBanned());
            userRepository.save(user);
        });
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public void removeFollow(Long followId) {
        followRepository.deleteById(followId);
    }

}
