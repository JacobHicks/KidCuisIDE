package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        if(id.equals("")) {
            model.addAttribute("login", new LoginBean());
            return "login";
        }
        else {  //TODO check if id is valid
            return "redirect:/code";
        }
    }

    @PostMapping("/login")
    public String loginRequest(@ModelAttribute LoginBean login, HttpServletResponse response) {
        String user = login.getUsername();
        String password = login.getPassword();
        if(isValidInput(user, password)) {
            return "redirect:/login";
        }
        else {
            String hash = Cryptor.hash("Kid" + user + "Cuis" + password + "IDE");
            response.addCookie(new Cookie("id", hash));
            //TODO check hash against DB
        }
        return "redirect:/code";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addCookie(new Cookie("id", ""));
        return "redirect:/login";
    }

    private boolean isValidInput(String user, String password) {
        return user == null || user.length() < 5 || user.length() > 64 || user.contains("\\W") || (user+password).contains("\\s") || password == null || password.length() < 6 || password.length() > 128;
    }
}