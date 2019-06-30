package com.seji.kidcuiside;

import com.seji.kidcuiside.forms.*;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    @PostMapping("/save")
    public void save(@CookieValue(value="session", defaultValue = "testUser") String sessionId, @RequestBody PreRunRequest pr) {
        FullRunRequest runRequest = new FullRunRequest(pr);
        runRequest.setSessionId(sessionId);
        ProcessBuilder processBuilder = new ProcessBuilder();

        File file = saveCode(runRequest);
        if(file != null) {
            switch (runRequest.getLanguage()) {
                case ("java"):
                    processBuilder.command("C:\\Program Files\\Java\\jdk1.8.0_201\\bin\\javac.exe", file.getAbsolutePath());
                    break;
            }
            Process process = null;
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (process.isAlive());
        }
    }

    private File saveCode(FullRunRequest runRequest) {
        try {
            File file = new File("Users/" + runRequest.getSessionId() + "/" + runRequest.getPath());
            if(!file.exists()) file.mkdir();
            file = new File("Users/" + runRequest.getSessionId() + "/" + runRequest.getPath() + "/" + runRequest.getName() + "." + runRequest.getLanguage());
            if (file.exists()) file.delete();
            System.out.println(file.getAbsolutePath());
            if (file.createNewFile()) {
                PrintWriter outputStream = new PrintWriter(file);
                outputStream.print(runRequest.getCode());
                outputStream.close();
                return file;
            } else {
                throw new IOException("Unable to create new file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}