package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Controller
public class CodeWindowController {

    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        if(id.equals("")) return "redirect:/login";
        return "CodeWritingWindow";
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/easyui/**").addResourceLocations("classpath:/easyui/");
        registry.addResourceHandler("/codemirror/**").addResourceLocations("classpath:/codemirror/");
        registry.addResourceHandler("/easyui/**").addResourceLocations("classpath:/easyui/");
    }
}
