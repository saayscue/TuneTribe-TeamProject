package com.assign.TuneTribe.admin;

import com.assign.TuneTribe.mod.Mod;
import com.assign.TuneTribe.mod.ModRepository;
import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author shauna
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    ModRepository repo;

    @Autowired
    UserRepository userRepo;

    public User getUser(long id) {
        return userRepo.getReferenceById(id);
    }

    public List<User> getUsers() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream()
                .filter(user -> !user.getRole().equals("Admin"))
                .filter(user -> !user.getRole().equals("Mod"))
                .filter(user -> !user.getRole().equals("Artist"))
                .collect(Collectors.toList());
    }

    public List<User> getArtists() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream()
                .filter(user -> !user.getRole().equals("Admin"))
                .filter(user -> !user.getRole().equals("Mod"))
                .filter(user -> !user.getRole().equals("User"))
                .collect(Collectors.toList());
    }

    public List<User> getMods() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream()
                .filter(user -> !user.getRole().equals("Admin"))
                .filter(user -> !user.getRole().equals("Artist"))
                .filter(user -> !user.getRole().equals("User"))
                .collect(Collectors.toList());
    }

    public User getUserByUserName(String userName) {
        return userRepo.findByUserName(userName).orElseThrow(()
                -> new UsernameNotFoundException(userName + "not found"));
    }

    public void toggleUserBan(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        userOptional.ifPresent(user -> {
            user.setBanned(!user.isBanned());
            userRepo.save(user);
        });
    }

    public boolean isUserBanned(String username) {
        Optional<User> userOptional = userRepo.findByUserName(username);
        return userOptional.map(User::isBanned).orElse(false);
    }

    public void deleteUser(long id) {
        userRepo.deleteById(id);
    }

    public void saveCommunityGuidelines(String guidelinesText) {
        Admin admin = adminRepository.findById(1L).orElse(new Admin()); // Assuming there's only one admin
        admin.setCommunityGuidelines(guidelinesText);
        adminRepository.save(admin);
    }

    public String getCommunityGuidelines() {
        return adminRepository.findById(1L)
                .map(Admin::getCommunityGuidelines)
                .orElse("");
    }

    public void saveCopyRight(String copyrightText) {
        Admin admin = adminRepository.findById(1L).orElse(new Admin()); // Assuming there's only one admin
        admin.setCopyright(copyrightText);
        adminRepository.save(admin);
    }

    public String getCopyRight() {
        return adminRepository.findById(1L)
                .map(Admin::getCopyright)
                .orElse("");
    }

    public long getTotalUsers() {
        return userRepo.count(); // Count all users in the repository
    }

    public List<Mod> getAllRequests() {
        return repo.findAll();
    }
}
