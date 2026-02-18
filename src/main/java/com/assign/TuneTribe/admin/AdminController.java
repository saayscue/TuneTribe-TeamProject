/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assign.TuneTribe.admin;

import com.assign.TuneTribe.mod.Mod;
import com.assign.TuneTribe.mod.ModService;
import com.assign.TuneTribe.user.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author shauna
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService service;

    @Autowired
    private ModService modService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/user")
    public String getUsers(Model model,
            @RequestParam(name = "continue", required = false) String cont,
            @RequestParam(name = "role", required = false) String role,
            @RequestParam(name = "status", required = false) String status) {
        model.addAttribute("userList", adminService.getUsersFiltered(role, status));
        model.addAttribute("roleFilter", role == null ? "all" : role);
        model.addAttribute("statusFilter", status == null ? "all" : status);
        return "admin/list-users";
    }

    @GetMapping("/artist")
    public String getArtists(Model model, @RequestParam(name = "continue", required = false) String cont) {
        model.addAttribute("userList", adminService.getArtists());
        return "admin/list-artists";
    }

    @GetMapping("/tunetribe")
    public String login() {
        return "admin/tunetribe";
    }

    @GetMapping("/user/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/user";
    }

    @PostMapping("/user/{userId}/toggle-ban")
    public String toggleUserBan(@PathVariable Long userId) {
        adminService.toggleUserBan(userId);
        return "redirect:/admin/user"; // Redirect back to the user list page
    }

    @PostMapping("/user/{userId}/make-mod")
    public String makeModerator(@PathVariable Long userId) {
        adminService.makeModerator(userId);
        return "redirect:/admin/user";
    }

    @GetMapping("/artist/{userID}")
    public String deleteArtist(@PathVariable Long userID) {
        adminService.deleteUser(userID);
        return "redirect:/admin/artist";
    }

    @PostMapping("/artist/{userId}/toggle-ban")
    public String toggleArtistBan(@PathVariable Long userId) {
        adminService.toggleUserBan(userId);
        return "redirect:/admin/artist"; // Redirect back to the user list page
    }

    @GetMapping("/moderator")
    public String getMods(Model model, @RequestParam(name = "continue", required = false) String cont) {
        model.addAttribute("userList", adminService.getMods());
        return "admin/list-mod";
    }

    @PostMapping("/moderator/{modId}/remove")
    public String removeModerator(@PathVariable Long modId) {
        adminService.removeModerator(modId);
        return "redirect:/admin/user";
    }

    @GetMapping("/modRequests")
    public String viewRequests(Model model) {
        List<Mod> modRequests = adminService.getAllRequests(); // Assuming you have a service for managing mod requests
        model.addAttribute("modRequests", modRequests);

        return "admin/modRequests";
    }

    @GetMapping("/updates")
    public String getUpdatesForm(Model model) {
        return "admin/updates";
    }

    @GetMapping("/community-guidelines")
    public String getCommunityGuidelinesForm(Model model) {
        String guidelines = adminService.getCommunityGuidelines();
        model.addAttribute("guidelines", guidelines);
        return "admin/community-guidelines-form";
    }

    @PostMapping("/save-community-guidelines")
    public String saveCommunityGuidelines(@RequestParam("guidelines") String guidelinesText) {
        adminService.saveCommunityGuidelines(guidelinesText);
        return "redirect:/admin/community-guidelines";
    }

    @GetMapping("/tunetribe-guidelines")
    public String getTuneTribeGuidelines(Model model) {
        String guidelines = adminService.getCommunityGuidelines();
        model.addAttribute("guidelines", guidelines);
        return "admin/tunetribe-guidelines";
    }

    @GetMapping("/copyright-rules")
    public String getCopyRightForm(Model model) {
        String copyright = adminService.getCopyRight();
        model.addAttribute("copyright", copyright);
        return "admin/copyright-form";
    }

    @PostMapping("/save-copyright-rules")
    public String saveCopyRight(@RequestParam("copyright") String copyrightText) {
        adminService.saveCopyRight(copyrightText);
        return "redirect:/admin/copyright-rules";
    }

    @GetMapping("/tunetribe-copyright")
    public String getTuneTribeCopyright(Model model) {
        String copyright = adminService.getCopyRight();
        model.addAttribute("copyright", copyright);
        return "admin/tunetribe-copyright";
    }

    @GetMapping("/user/id={id}")
    public String getUserProfile(@PathVariable long id, Model model) {
        model.addAttribute("user", adminService.getUser(id));
        return "admin/user-profile";
    }

}
