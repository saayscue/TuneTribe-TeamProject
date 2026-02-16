/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assign.TuneTribe.user;

import com.assign.TuneTribe.follow.FollowService;
import com.assign.TuneTribe.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author shauna
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private FollowService followService;

    @GetMapping("/profile")
    public String viewOwnProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userRepository.findByUserName(auth.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        if (Boolean.TRUE.equals(user.isBanned())) {
            return "redirect:/force-logout";
        }
        model.addAttribute("profileUser", user);
        model.addAttribute("posts", postService.getPostsForUser(user));
        model.addAttribute("totalPosts", postService.countPostsForUser(user));
        model.addAttribute("totalFollowers", followService.countFollowers(user));
        model.addAttribute("totalFollowing", followService.countFollowing(user));
        model.addAttribute("isOwnProfile", true);
        model.addAttribute("currentUserEntity", user);
        return "user-profile";
    }

    @GetMapping("/users/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/";
        }
        if (Boolean.TRUE.equals(user.isBanned())) {
            return "redirect:/";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User viewer = null;
        if (auth != null && auth.isAuthenticated()) {
            viewer = userRepository.findByUserName(auth.getName()).orElse(null);
        }
        if (viewer != null && Boolean.TRUE.equals(viewer.isBanned())) {
            return "redirect:/force-logout";
        }

        model.addAttribute("profileUser", user);
        model.addAttribute("posts", postService.getPostsForUser(user));
        model.addAttribute("totalPosts", postService.countPostsForUser(user));
        model.addAttribute("totalFollowers", followService.countFollowers(user));
        model.addAttribute("totalFollowing", followService.countFollowing(user));
        model.addAttribute("isOwnProfile", viewer != null && viewer.getId() == user.getId());
        model.addAttribute("isFollowing", viewer != null && followService.isFollowing(viewer, user));
        model.addAttribute("currentUserEntity", viewer);
        return "user-profile";
    }
}
