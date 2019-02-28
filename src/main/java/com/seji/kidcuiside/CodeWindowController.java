package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
    public String getCode(@ModelAttribute Code tcode, @CookieValue(value = "id", defaultValue = "") String id, @CookieValue(value = "user", defaultValue = "null") String user) {
        if(isAuthenticated(id, user)) {
            FileData code = new FileData("Main.java", tcode.getCode());
            //if (tcode.getRequest().equals("run")) {
                ProccessManager pm = new ProccessManager(user);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (pm.compile(code, System.in, System.out, System.err) == 0) { //TODO set params to the code window
                    try {
                        AtomicBoolean finished = new AtomicBoolean(false);
                        PrintStream output = System.out; //TODO set this to the code window
                        new Thread(() -> {
                            do {
                                try {
                                    if (baos.size() > 0) {
                                        output.print(baos.toString(String.valueOf(StandardCharsets.ISO_8859_1)));
                                        baos.close();
                                        baos.reset();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } while (!finished.get());
                        }).start();
                        pm.run(code, System.in, baos, System.err);
                        finished.set(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            /*} else */ //if (tcode.getRequest().equals("save")) {
                //code.write("Users/" + user);
            //}
            return "code";
        }
        return "redirect:/login";
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