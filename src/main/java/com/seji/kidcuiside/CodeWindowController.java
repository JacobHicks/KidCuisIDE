package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodeWindowController {
    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "NotLoggedIn") String id) {
        if(id.equals("NotLoggedIn")) return "redirect:/login";
        return "CodeWritingWindow";
    }
}
