package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodeWindowController {
    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        if(id.equals("")) return "redirect:/login";
        return "CodeWritingWindow";
    }
}
