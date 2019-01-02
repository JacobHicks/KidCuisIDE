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
        //TODO send data to server
    }
    @PostMapping("/login")
    public String loginRequest(@ModelAttribute LoginBean login) {
        String user = login.getUsername();
        String password = login.getPassword();
        if(user == null || user.length() < 5 || user.contains("\\W") || password == null || password.length() < 6) {
            return "login";
        }
        else {
            
        }
        return "result";
    }
}