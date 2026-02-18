package com.assign.TuneTribe;

import com.assign.TuneTribe.admin.AdminService;
import com.assign.TuneTribe.follow.FollowService;
import com.assign.TuneTribe.post.PostService;
import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import com.assign.TuneTribe.user.UserService;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;

@Controller
public class AppController {

    @Autowired
    UserRepository repo;

    @Autowired
    UserService service;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;

    @Autowired
    private FollowService followService;

    @GetMapping(value = { "", "/" })
    public String dashboard(Model model) {
        // Check if the user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"));
            boolean isMod = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Mod"));
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("isMod", isMod);

            String name = auth.getName();
            model.addAttribute("currentUser", name);

            User currentUser = repo.findByUserName(name).orElse(null);
            if (currentUser != null) {
                if (Boolean.TRUE.equals(currentUser.isBanned())) {
                    return "redirect:/force-logout";
                }
                model.addAttribute("posts", postService.getFeedForUser(currentUser));
                model.addAttribute("totalPosts", postService.countPostsForUser(currentUser));
                model.addAttribute("currentUserEntity", currentUser);
                model.addAttribute("totalFollowers", followService.countFollowers(currentUser));
                model.addAttribute("totalFollowing", followService.countFollowing(currentUser));

                List<User> suggestedUsers = repo.findByUserNameNotAndBannedFalse(currentUser.getUserName());
                model.addAttribute("suggestedUsers", suggestedUsers);

                List<User> trendingArtists = repo.findByRoleIgnoreCaseAndBannedFalse("Artist");
                model.addAttribute("trendingArtists", trendingArtists);

                model.addAttribute("recentTracks", postService.getRecentMusicPosts(5));
                model.addAttribute("updates", postService.getRecentPosts(5));
            } else {
                model.addAttribute("posts", Collections.emptyList());
                model.addAttribute("totalPosts", 0);
                model.addAttribute("currentUserEntity", null);
                model.addAttribute("suggestedUsers", Collections.emptyList());
                model.addAttribute("trendingArtists", Collections.emptyList());
                model.addAttribute("recentTracks", Collections.emptyList());
                model.addAttribute("updates", Collections.emptyList());
                model.addAttribute("totalFollowers", 0);
                model.addAttribute("totalFollowing", 0);
            }

            return "user";
        }
        return "redirect:/login";
    }

    @GetMapping("/force-logout")
    public String forceLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping(value = { "/admin/home" })
    public String admin(Model model) {
        long totalUsers = adminService.getTotalUsers();
        model.addAttribute("totalUsers", totalUsers);

        return "admin/admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/tunetribe-guidelines")
    public String viewGuidelines(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"));
        boolean isMod = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Mod"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isMod", isMod);
        String guidelines = adminService.getCommunityGuidelines();
        model.addAttribute("guidelines", guidelines);
        return "guidelines";
    }

    @GetMapping("/tunetribe-copyright")
    public String viewCopyright(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"));
        boolean isMod = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Mod"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isMod", isMod);
        String copyright = adminService.getCopyRight();
        model.addAttribute("copyright", copyright);
        return "copyright";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        service.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
    public ResponseEntity<Void> ignoreChromeDevtoolsRequest() {
        return ResponseEntity.noContent().build();
    }

}
