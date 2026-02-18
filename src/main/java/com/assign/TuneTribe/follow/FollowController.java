package com.assign.TuneTribe.follow;

import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users/{id}/follow")
    public String followUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        User follower = userRepository.findByUserName(auth.getName()).orElse(null);
        User following = userRepository.findById(id).orElse(null);
        if (follower == null || following == null) {
            return "redirect:/";
        }
        if (Boolean.TRUE.equals(follower.isBanned())) {
            return "redirect:/force-logout";
        }
        if (Boolean.TRUE.equals(following.isBanned())) {
            return "redirect:/";
        }
        followService.follow(follower, following);
        return "redirect:/users/" + id;
    }

    @PostMapping("/users/{id}/unfollow")
    public String unfollowUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        User follower = userRepository.findByUserName(auth.getName()).orElse(null);
        User following = userRepository.findById(id).orElse(null);
        if (follower == null || following == null) {
            return "redirect:/";
        }
        if (Boolean.TRUE.equals(follower.isBanned())) {
            return "redirect:/force-logout";
        }
        if (Boolean.TRUE.equals(following.isBanned())) {
            return "redirect:/";
        }
        followService.unfollow(follower, following);
        return "redirect:/users/" + id;
    }
}
