package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.Scanner;

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
                PrintStream ps = new PrintStream(outputStream);
                if (tcode.getRequest().equals("run")) {
                    ps.print("Compiling... ");
                    ProccessManager pm = new ProccessManager(user);
                    ByteArrayOutputStream errorstream = new ByteArrayOutputStream();
                    if (pm.compile(code, System.in, outputStream, errorstream) == 0) {
                        try {
                            pm.run(code, System.in, outputStream, errorstream);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ps.println(e.toString());
                        }
                    }
                    else {
                        String error = errorstream.toString();
                        ps.println("\n" + error.substring(error.indexOf("KidCuisIDE\\Users\\") + "KidCuisIDE\\Users\\".length()));
                        System.out.println("\n Compilation Failed");
                    }
                    System.out.println("dbg" + outputStream.toString() + errorstream.toString());
                }
                else if (tcode.getRequest().equals("save")) {
                    ps.println("Saving... ");
                    code.write("Users/" + user);
                    ps.print("Success! ");
                }
                ps.close();
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