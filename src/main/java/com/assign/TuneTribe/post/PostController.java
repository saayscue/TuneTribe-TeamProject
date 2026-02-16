package com.assign.TuneTribe.post;

import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author shauna
 */
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts")
    public String createPost(@RequestParam("content") String content,
            @RequestParam(value = "musicTitle", required = false) String musicTitle,
            @RequestParam(value = "musicUrl", required = false) String musicUrl) {
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
        boolean isArtist = user.getRole() != null && user.getRole().equalsIgnoreCase("Artist");
        if (!isArtist) {
            musicTitle = null;
            musicUrl = null;
        }
        postService.createPost(user, content, musicTitle, musicUrl);
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,
            @RequestHeader(value = "Referer", required = false) String referer) {
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
        postService.deletePost(user, id);
        return referer != null ? "redirect:" + referer : "redirect:/";
    }

    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id,
            @RequestParam("content") String content,
            @RequestParam(value = "musicTitle", required = false) String musicTitle,
            @RequestParam(value = "musicUrl", required = false) String musicUrl,
            @RequestHeader(value = "Referer", required = false) String referer) {
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
        boolean isArtist = user.getRole() != null && user.getRole().equalsIgnoreCase("Artist");
        if (!isArtist) {
            musicTitle = null;
            musicUrl = null;
        }
        postService.updatePost(user, id, content, musicTitle, musicUrl);
        return referer != null ? "redirect:" + referer : "redirect:/";
    }
}
