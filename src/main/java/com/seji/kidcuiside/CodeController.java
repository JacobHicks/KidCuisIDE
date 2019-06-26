package com.seji.kidcuiside;
import com.seji.kidcuiside.forms.ConsoleOutput;
import com.seji.kidcuiside.forms.FullRunRequest;
import com.seji.kidcuiside.forms.PreRunRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://127.0.0.1", allowCredentials = "true")
@RestController
public class CodeController {
    @PostMapping("/run")
    public void requestUpload(@RequestBody PreRunRequest preRunRequest, @CookieValue(value = "session", defaultValue = "testUser") String sessionId, HttpServletResponse servletResponse) { //TODO remove test user
        FullRunRequest runRequest = new FullRunRequest(preRunRequest);
        runRequest.setSessionId(sessionId);
        Mailbox.addRequest(runRequest);
        //TODO add messaging here!!
        servletResponse.setStatus(200);
    }

    @GetMapping("/output")
    public @ResponseBody ConsoleOutput checkMail(@CookieValue(value = "session", defaultValue = "testUser") String sessionId) {
        ConsoleOutput res = Mailbox.getMail(sessionId);
        return res;
    }
}