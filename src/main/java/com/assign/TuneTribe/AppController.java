package com.assign.TuneTribe;

import com.assign.TuneTribe.admin.AdminService;
import com.assign.TuneTribe.user.User;
import com.assign.TuneTribe.user.UserRepository;
import com.assign.TuneTribe.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author shauna
 */
@Controller
public class AppController {

    @Autowired
    UserRepository repo;

    @Autowired
    UserService service;
    
    
    @Autowired
    private AdminService adminService;

    @GetMapping(value = {"", "/"})
    public String dashboard(Model model) {
        // Check if the user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            // Check if the user has the admin role
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"))) {
                long totalUsers = adminService.getTotalUsers();
        model.addAttribute("totalUsers", totalUsers);
        
                return "admin/admin"; // Redirect admin users to admin page
            } else {
                // Regular users go to root page
                String name = auth.getName();
                model.addAttribute("currentUser", name);
                return "user";
            }
        }
        return "redirect:/login"; // Redirect unauthenticated users to login page
    }

    @GetMapping(value = {"/admin/home"})
    public String admin(Model model) {
         long totalUsers = adminService.getTotalUsers();
        model.addAttribute("totalUsers", totalUsers);
        
    return "admin/admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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

   
}
