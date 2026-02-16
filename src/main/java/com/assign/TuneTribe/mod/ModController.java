package com.assign.TuneTribe.mod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author shauna
 */
@Controller
@RequestMapping("/mod")
public class ModController {

    @Autowired
    private ModService modService;

    @GetMapping("/home")
    public String home() {
        return "mod/mod";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("userList", modService.getUsersForModeration());
        return "mod/list-users";
    }

    @PostMapping("/users/{userId}/toggle-ban")
    public String toggleUserBan(@PathVariable Long userId) {
        modService.toggleUserBan(userId);
        return "redirect:/mod/users";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("postList", modService.getPostsForModeration());
        return "mod/list-posts";
    }

    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable Long postId) {
        modService.deletePost(postId);
        return "redirect:/mod/posts";
    }

    @GetMapping("/follows")
    public String follows(Model model) {
        model.addAttribute("followList", modService.getFollowsForModeration());
        return "mod/list-follows";
    }

    @PostMapping("/follows/{followId}/remove")
    public String removeFollow(@PathVariable Long followId) {
        modService.removeFollow(followId);
        return "redirect:/mod/follows";
    }
}
