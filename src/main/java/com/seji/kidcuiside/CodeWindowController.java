package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;

@Controller
public class CodeWindowController {
    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        if(id.equals("")) return "redirect:/login";
        return "CodeWritingWindow";

    }

    @RequestMapping(value = "/save.htm", method = RequestMethod.GET)
    public void saver() throws Exception {
        //DONT FORGET TO CHANGE THIS STUFF DEPENDING ON WHERE YOU KEEP YOUR PROJECT FILES
        //what you see below works only for my own setup
        String slash = File.separator;
        String userFile = "C:" + slash + "Users" + slash + "vargh" + slash + "Documents" + slash + "kidcuisIDE" +
                slash + "USERNAMEHERE" + slash + "PROJECTNAMEHERE.txt";
        File f = new File(userFile);

        f.getParentFile().mkdirs();
        f.createNewFile();
    }
}
