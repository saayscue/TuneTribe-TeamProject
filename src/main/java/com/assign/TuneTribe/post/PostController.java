package com.assign.TuneTribe.post;

import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import com.assign.TuneTribe.spotify.SpotifyService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpotifyService spotifyService;

    @PostMapping("/posts")
    public String createPost(@RequestParam("content") String content,
            @RequestParam(value = "musicTitle", required = false) String musicTitle,
            @RequestParam(value = "musicArtist", required = false) String musicArtist) {
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
        String musicUrl = null;
        if (!isArtist(user)) {
            musicTitle = null;
        } else if (musicTitle != null && !musicTitle.isBlank()) {
            Optional<SpotifyService.TrackInfo> trackInfo = spotifyService.searchTrack(musicTitle, musicArtist);
            if (trackInfo.isPresent()) {
                musicTitle = trackInfo.get().name();
                musicUrl = trackInfo.get().url();
            } else {
                musicTitle = null;
            }
        } else {
            musicTitle = null;
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
            @RequestParam(value = "musicArtist", required = false) String musicArtist,
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
        String musicUrl = null;
        if (!isArtist(user)) {
            musicTitle = null;
        } else if (musicTitle != null && !musicTitle.isBlank()) {
            Optional<SpotifyService.TrackInfo> trackInfo = spotifyService.searchTrack(musicTitle, musicArtist);
            if (trackInfo.isPresent()) {
                musicTitle = trackInfo.get().name();
                musicUrl = trackInfo.get().url();
            } else {
                musicTitle = null;
            }
        } else {
            musicTitle = null;
        }
        postService.updatePost(user, id, content, musicTitle, musicUrl);
        return referer != null ? "redirect:" + referer : "redirect:/";
    }

    private boolean isArtist(User user) {
        if (user == null) {
            return false;
        }
        if (user.getRole() != null && user.getRole().equalsIgnoreCase("Artist")) {
            return true;
        }
        return user.getOriginalRole() != null && user.getOriginalRole().equalsIgnoreCase("Artist");
    }
}
