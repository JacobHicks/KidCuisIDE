package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class CodeWindowController {

    @GetMapping("/code")
    public String code(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        if(id.equals("")) return "redirect:/login";
        model.addAttribute("code", new Code());
        return "CodeWritingWindow";
    }

    @PostMapping("/code")
    public String getCode(@ModelAttribute Code tcode) { //TODO use FileData object
        FileData code = new FileData(0, "Main.java", tcode.getCode());

        ProccessManager pm = new ProccessManager("");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(pm.compile(code, System.in, System.out, System.err) == 0) { //TODO set params to the code window
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
                new Thread(() -> {
                    pm.run(code, System.in, baos, System.err);
                    finished.set(true);
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "code";
    }

}
