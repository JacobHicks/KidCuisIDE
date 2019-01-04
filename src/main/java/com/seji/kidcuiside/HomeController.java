package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public static String home() {
        return "home";
    }
}
