package com.assign.TuneTribe.post;

import com.assign.TuneTribe.user.User;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void createPost(User user, String content, String musicTitle, String musicUrl) {
        if (user == null) {
            return;
        }
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(content.trim());
        if (musicTitle != null && !musicTitle.trim().isEmpty()) {
            post.setMusicTitle(musicTitle.trim());
        }
        if (musicUrl != null && !musicUrl.trim().isEmpty()) {
            post.setMusicUrl(musicUrl.trim());
        }
        postRepository.save(post);
    }

    public List<Post> getFeedForUser(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        return postRepository.findAllByUser_BannedFalseOrderByCreatedAtDesc();
    }

    public List<Post> getPostsForUser(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        if (Boolean.TRUE.equals(user.isBanned())) {
            return Collections.emptyList();
        }
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public boolean deletePost(User user, Long postId) {
        if (user == null || postId == null) {
            return false;
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getUser() == null) {
            return false;
        }
        if (post.getUser().getId() != user.getId()) {
            return false;
        }
        postRepository.delete(post);
        return true;
    }

    @Transactional
    public boolean updatePost(User user, Long postId, String content, String musicTitle, String musicUrl) {
        if (user == null || postId == null) {
            return false;
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getUser() == null) {
            return false;
        }
        if (post.getUser().getId() != user.getId()) {
            return false;
        }
        if (content != null && !content.trim().isEmpty()) {
            post.setContent(content.trim());
        }
        if (musicTitle != null && !musicTitle.trim().isEmpty()) {
            post.setMusicTitle(musicTitle.trim());
        } else {
            post.setMusicTitle(null);
        }
        if (musicUrl != null && !musicUrl.trim().isEmpty()) {
            post.setMusicUrl(musicUrl.trim());
        } else {
            post.setMusicUrl(null);
        }
        postRepository.save(post);
        return true;
    }

    public long countPostsForUser(User user) {
        if (user == null) {
            return 0;
        }
        return postRepository.countByUser(user);
    }

    public List<Post> getRecentPosts(int limit) {
        List<Post> posts = postRepository.findTop10ByUser_BannedFalseOrderByCreatedAtDesc();
        if (posts == null) {
            return Collections.emptyList();
        }
        return posts.size() > limit ? posts.subList(0, limit) : posts;
    }

    public List<Post> getRecentMusicPosts(int limit) {
        List<Post> posts = postRepository.findTop10ByUser_BannedFalseOrderByCreatedAtDesc();
        if (posts == null) {
            return Collections.emptyList();
        }
        return posts.stream()
                .filter(post -> post.getMusicTitle() != null && !post.getMusicTitle().isBlank())
                .limit(limit)
                .toList();
    }
}
