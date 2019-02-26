package com.seji.kidcuiside;

import org.springframework.web.bind.annotation.CookieValue;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ProccessManager {
    final private JavaCompiler javacompiler = ToolProvider.getSystemJavaCompiler();
    private String user;

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

    public void run(FileData fileData, InputStream input, OutputStream output, OutputStream error) { //TODO allow project support
        try {
            URLClassLoader directory = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + "/" + user + "/RUN/")});
            Class selection = Class.forName(fileData.getName().substring(0, fileData.getName().length() - ".java".length()), true, directory);
            Method main = selection.getDeclaredMethod("main", String[].class);
            PrintStream userOut = new PrintStream(output);
            PrintStream userErr = new PrintStream(error);
            PrintStream systemOut = System.out;
            PrintStream systemErr = System.err;
            InputStream systemIn = System.in;
            System.setOut(userOut);
            System.setErr(userErr);
            System.setIn(input);
            try {
                main.invoke(null, (Object) new String[]{"test"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.flush();
            System.err.flush();
            System.setOut(systemOut);
            System.setErr(systemErr);
            System.setIn(systemIn);
        } catch (ClassNotFoundException | NoSuchMethodException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
