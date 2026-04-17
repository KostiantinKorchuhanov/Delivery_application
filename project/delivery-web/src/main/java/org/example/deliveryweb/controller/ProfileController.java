package org.example.deliveryweb.controller;

import com.example.entity.user.User;
import com.example.validation.GeneralValidation;
import jakarta.servlet.http.HttpSession;
import org.example.deliveryweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    private final GeneralValidation generalValidation = new GeneralValidation();

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile() {
        return "profile_page";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("phoneNumber") String phoneNumber,
                                HttpSession session) {

        if (!generalValidation.validateUsername(username)
                || !generalValidation.validateEmail(email)
                || !generalValidation.validatePhone(phoneNumber)) {
            return "redirect:/profile?error";
        }
        User currentUser = (User) session.getAttribute("user");
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setPhoneNumber(phoneNumber);
        userRepository.save(currentUser);
        return "redirect:/profile?updated";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        userRepository.delete(currentUser);
        session.invalidate();
        return "redirect:/login?deleted";
    }
}