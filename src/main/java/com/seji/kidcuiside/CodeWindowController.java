package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class CodeWindowController {

    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "") String id, @CookieValue(value = "user", defaultValue = "") String user) {
        if(isAuthenticated(id, user)) {
            model.addAttribute("code", new Code());
            return "CodeWritingWindow";
        }
        return "redirect:/login";
    }

    @PostMapping("/code")
    @ResponseBody
    public StreamingResponseBody getCode(@ModelAttribute Code tcode, @CookieValue(value = "id", defaultValue = "") String id, @CookieValue(value = "user", defaultValue = "null") String user) {
        return (outputStream) -> {
            if (isAuthenticated(id, user)) {
                FileData code = new FileData("Main.java", tcode.getCode());
                //if (tcode.getRequest().equals("run")) {
                ProccessManager pm = new ProccessManager(user);

                if (pm.compile(code, System.in, outputStream, outputStream) == 0) { //TODO set params to code window
                    try {
                        pm.run(code, System.in, outputStream, outputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*} else */ //if (tcode.getRequest().equals("save")) {
                //code.write("Users/" + user);
                //return null;
                //}
            }
        };
    }

    public boolean isAuthenticated(String id, String user) {
        File userdir = new File("Users/" + user);
        if(userdir.exists()) {
            try {
                Scanner hashreader = new Scanner(new File("Users/" + user + "/hash"));
                if (hashreader.nextLine().equals(id)) {
                    hashreader.close();
                    return true;
                }
                hashreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}