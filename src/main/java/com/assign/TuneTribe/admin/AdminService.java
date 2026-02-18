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
        return getUsersFiltered(null, null);
    }

    public List<User> getUsersFiltered(String role, String status) {
        List<User> allUsers = userRepo.findAll();

        return allUsers.stream()
                .filter(user -> user.getRole() != null)
                .filter(user -> !user.getRole().equalsIgnoreCase("Admin"))
                .filter(user -> {
                    if (role == null || role.isBlank() || role.equalsIgnoreCase("all")) {
                        return user.getRole().equalsIgnoreCase("User")
                                || user.getRole().equalsIgnoreCase("Artist")
                                || user.getRole().equalsIgnoreCase("Mod");
                    }
                    return user.getRole().equalsIgnoreCase(role);
                })
                .filter(user -> {
                    if (status == null || status.isBlank() || status.equalsIgnoreCase("all")) {
                        return true;
                    }
                    if (status.equalsIgnoreCase("active")) {
                        return !user.isBanned();
                    }
                    if (status.equalsIgnoreCase("banned")) {
                        return user.isBanned();
                    }
                    return true;
                })
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
        return userRepo.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName + "not found"));
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

    public void makeModerator(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        userOptional.ifPresent(user -> {
            if (user.getOriginalRole() == null || user.getOriginalRole().isBlank()) {
                user.setOriginalRole(user.getRole());
            }
            user.setRole("Mod");
            userRepo.save(user);
        });
    }

    public void removeModerator(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        userOptional.ifPresent(user -> {
            if ("Mod".equalsIgnoreCase(user.getRole())) {
                String previousRole = user.getOriginalRole();
                if (previousRole != null && !previousRole.isBlank() && !"Mod".equalsIgnoreCase(previousRole)) {
                    user.setRole(previousRole);
                } else {
                    user.setRole("User");
                }
                user.setOriginalRole(null);
                userRepo.save(user);
            }
        });
    }

    public void saveCommunityGuidelines(String guidelinesText) {
        Admin admin = adminRepository.findFirstByOrderByAdminIdAsc();
        if (admin == null) {
            admin = new Admin();
        }
        admin.setCommunityGuidelines(guidelinesText);
        adminRepository.save(admin);
    }

    public String getCommunityGuidelines() {
        Admin admin = adminRepository.findFirstByOrderByAdminIdAsc();
        return admin != null ? admin.getCommunityGuidelines() : "";
    }

    public void saveCopyRight(String copyrightText) {
        Admin admin = adminRepository.findFirstByOrderByAdminIdAsc();
        if (admin == null) {
            admin = new Admin();
        }
        admin.setCopyright(copyrightText);
        adminRepository.save(admin);
    }

    public String getCopyRight() {
        Admin admin = adminRepository.findFirstByOrderByAdminIdAsc();
        return admin != null ? admin.getCopyright() : "";
    }

    public long getTotalUsers() {
        return userRepo.count();
    }

    public List<Mod> getAllRequests() {
        return repo.findAll();
    }
}
