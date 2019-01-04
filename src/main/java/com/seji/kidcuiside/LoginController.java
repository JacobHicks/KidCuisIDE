package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        /*FileData fd = new FileData(0, "Main.java",
                "public class Main {" +
                "\n    public static void main(String[] args) {" +
                "\n        System.out.println(\"Hello World!\");" +
                "\n    }" +
                "\n }");
        fd.write("TESTUSER");
        ProccessManager pm = new ProccessManager("");
        pm.compile(fd, System.in, System.out, System.err);
        pm.run(fd, null, null, null);
        **********Test Stuff*************
        */
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
            String hash = Cryptor.hash("Kid" + user + "Cuis" + password + "IDE");
            response.addCookie(new Cookie("id", hash));
            //TODO check hash against DB
        }
        return "redirect:/code";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("signup", new SignUpBean());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignUpBean signup) {
        System.out.println(signup.getFirstname());
        if(signup.getPassword().equals(signup.getConfirmPassword()) && isValidInput(signup.getUsername(), signup.getPassword())) {
            String hash = Cryptor.hash("Kid" + signup.getUsername() + "Cuis" + signup.getPassword() + "IDE");
            return "redirect:/signupconfirm";
            //TODO register user in db
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