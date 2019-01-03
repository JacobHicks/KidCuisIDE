package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new LoginBean());
        return "login";
    }

    @PostMapping("/login")
    public String loginRequest(@ModelAttribute LoginBean login) {
        String user = login.getUsername();
        String password = login.getPassword();
        if(user == null || user.length() < 5 || user.length() > 64 || user.contains("\\W") || (user+password).contains("\\s") || password == null || password.length() < 6 || password.length() > 128) {
            return "redirect:/login";
        }
        else {
            String hash = Cryptor.hash("Kid" + user + "Cuis" + password + "IDE");
            //TODO check hash against DB
        }
        return "redirect:/CodeWritingWindow";
    }
}