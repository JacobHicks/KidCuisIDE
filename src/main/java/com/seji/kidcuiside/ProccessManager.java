package com.seji.kidcuiside;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.web.bind.annotation.CookieValue;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;

public class ProccessManager {
    final JavaCompiler javacompiler = ToolProvider.getSystemJavaCompiler();
    Process process;
    String user;

    ProccessManager(@CookieValue(value = "id", defaultValue = "") String id) {
        //TODO Check if id is valid then find username from id
        user = "TESTUSER";
    }

    public int compile(FileData fileData, InputStream input, OutputStream output, OutputStream error) {
        File file = new File(user + "/RUN");
        file.mkdirs();
        file = new File(user + "/RUN/" + fileData.getName());
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.print(fileData.getData());
            pw.close();
            if(fileData.getName().endsWith(".java")) {
                return javacompiler.run(input, output, error, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public void run(FileData fileData, InputStream input, OutputStream output, OutputStream error) {
        try {
            System.out.println("cmd /c java C:\\Users\\hicks\\IdeaProjects\\KidCuisIDE\\TESTUSER\\RUN\\" + fileData.getName().substring(0,fileData.getName().length()-5) + ".class");
            process = Runtime.getRuntime().exec("cmd /c java C:\\Users\\hicks\\IdeaProjects\\KidCuisIDE\\TESTUSER\\RUN\\" + fileData.getName().substring(0,fileData.getName().length()-5) + ".class");
            System.out.println(process.getInputStream().available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
