package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

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
        if(!isValidInput(user, password)) {
            return "redirect:/login";
        }
        else {
            try {
                String hash = Cryptor.hash("Kid" + user + "Cuis" + password + "IDE");
                File userdir = new File("Users/" + user);
                if(userdir.exists()) {
                    Scanner hashreader = new Scanner(new File("Users/" + user + "/hash"));
                    if(hashreader.nextLine().equals(hash)) {
                        response.addCookie(new Cookie("id", hash));
                        response.addCookie(new Cookie("user", user)); //TODO add more security
                        hashreader.close();
                        return "redirect:/code";
                    }
                    hashreader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/login";
            }
        }
        return "redirect:/login"; //TODO tell wrong credentials
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("signup", new SignUpBean());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignUpBean signup) {
        System.out.println(signup);
        if(signup.getPassword().equals(signup.getConfirmPassword()) && isValidInput(signup.getUsername(), signup.getPassword())) {
            String hash = Cryptor.hash("Kid" + signup.getUsername() + "Cuis" + signup.getPassword() + "IDE");
            File hashfile = new File("Users/" + signup.getUsername());
            if(!hashfile.exists()) {
                try {
                    hashfile.mkdirs();
                    hashfile = new File("Users/" + signup.getUsername() + "/hash");
                    hashfile.createNewFile();
                    PrintWriter hashwriter = new PrintWriter(hashfile);
                    hashwriter.println(hash);
                    hashwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "redirect:/error";
                }
                return "redirect:/";
            } //TODO tell user that name is taken
        }
        //Try again skrub
        return "redirect:/signup";
    }

    @GetMapping("/signupconfirm")
    public String signUpConfirm() {
        return "signupConfirm";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addCookie(new Cookie("id", ""));
        return "redirect:/";
    }

    private boolean isValidInput(String user, String password) {
        return !(user == null || user.length() < 5 || user.length() > 64 || user.contains("\\W") || (user+password).contains("\\s") || password == null || password.length() < 6 || password.length() > 128);
    }
}