package com.seji.kidcuiside;

import com.seji.kidcuiside.forms.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "http://127.0.0.1", allowCredentials = "true")
@RestController
public class CodeController {
    private static int nodeIndex = 0;

    @PostMapping("/run")
    public void requestUpload(@RequestBody PreRunRequest preRunRequest, @CookieValue(value = "session", defaultValue = "testUser") String sessionId, HttpServletResponse servletResponse) { //TODO remove test user
        FullRunRequest runRequest = new FullRunRequest(preRunRequest);
        runRequest.setSessionId(sessionId);
        Mailbox.addRequest(runRequest);
        //TODO add messaging here!!
        servletResponse.setStatus(200);
    }

    @PostMapping("/send")
    public void sendMail(@RequestBody String message, @CookieValue(value = "session", defaultValue = "testUser") String sessionId, HttpServletResponse servletResponse) {
        Mailbox.sendMail(sessionId, message);
    }

    @GetMapping("/output")
    public @ResponseBody
    ConsoleOutput checkMail(@CookieValue(value = "session", defaultValue = "testUser") String sessionId) {
        return Mailbox.getMail(sessionId);
    }

    @PostMapping("/stop")
    public void kill(@CookieValue(value = "session", defaultValue = "testUser") String sessionId) {
        Mailbox.stop(sessionId);
    }

    @PostMapping("/done")
    public void done(@CookieValue(value = "session", defaultValue = "testUser") String sessionId) {
        Mailbox.stop(sessionId);
        File dir = new File("Users/" + sessionId);
        dir.delete();
    }

    @PostMapping("/new-file")
    public void newDir(@CookieValue(value = "session", defaultValue = "testUser") String sessionId, @RequestBody NewRequest request) {
        if (request.getPath().contains("..")) {
            //todo
        } else if (request.getType().equals("folder")) {
            File newDir = new File("Users/" + sessionId + "/" + request.getPath() + "/" + request.getName());
            newDir.mkdirs();
        } else if (request.getType().equals("java source file")) {
            generateGenericFile(sessionId, request, ".java");
        } else if (request.getType().equals("text file")) {
            generateGenericFile(sessionId, request, ".txt");
        } else if (request.getType().equals("file")) {
            generateGenericFile(sessionId, request, "");
        }
    }

    private void generateGenericFile(String sessionId, NewRequest request, String s) {
        File newDir = new File("Users/" + sessionId + "/" + request.getPath());
        newDir.mkdirs();
        File newFile = new File("Users/" + sessionId + "/" + request.getPath() + "/" + request.getName() + s);
        if (newFile.exists()) newFile.delete();
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            //todo
        }
    }

    @GetMapping("/tree")
    public @ResponseBody
    Collection<TreeNode> getTree(@CookieValue(value = "session", defaultValue = "testUser") String sessionId) {
        ArrayList<TreeNode> res = new ArrayList<>();
        File rootDir = new File("Users/" + sessionId);
        treeBuilder(sessionId, res, rootDir, -1);
        nodeIndex = 0;
        return res;
    }

    private void treeBuilder(String sessionId, ArrayList<TreeNode> res, File node, int parent) {
        TreeNode treeNode = new TreeNode();
        treeNode.setParent(parent);
        int nindex = res.size();
        res.add(treeNode);
        int index = node.getName().indexOf(".");
        if (index == -1) {
            treeNode.setType("folder");
            treeNode.setName(node.getName());
        } else {
            treeNode.setType(node.getName().substring(index + 1));
            treeNode.setName(node.getName().substring(0, index));
        }
        treeNode.setOpen(false);
        treeNode.setRoot(node.getParentFile().getName().equals(sessionId));
        File[] children = node.listFiles();
        if (children != null) {
            Integer[] newChildrenIndexes = new Integer[children.length];
            for (int i = 0; i < children.length; i++) {
                newChildrenIndexes[i] = ++nodeIndex;
                treeBuilder(sessionId, res, children[i], nindex);
            }
            treeNode.setChildren(newChildrenIndexes);
        }
    }

    @PostMapping("/load")
    public @ResponseBody Collection<String> loadCode(@CookieValue(value = "session", defaultValue = "testUser") String sessionId, @RequestBody String fileStr) {
        ArrayList<String> res = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File("Users/" + sessionId + "/" + fileStr));
            String code = "";
            sc.useDelimiter("");
            while (sc.hasNext()) code += sc.next();
            res.add(code);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
}