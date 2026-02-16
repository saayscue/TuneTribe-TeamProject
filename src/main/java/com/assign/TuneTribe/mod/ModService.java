/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shauna
 */
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

    public List<User> getUsersForModeration() {
        return userRepository.findAll().stream()
                .filter(user -> !"Admin".equalsIgnoreCase(user.getRole()))
                .collect(Collectors.toList());
    }

    public List<Post> getPostsForModeration() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Follow> getFollowsForModeration() {
        return followRepository.findAll();
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
