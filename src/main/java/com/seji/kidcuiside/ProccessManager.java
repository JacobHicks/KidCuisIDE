package com.seji.kidcuiside;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ProccessManager {
    final private JavaCompiler javacompiler = ToolProvider.getSystemJavaCompiler();
    private String user;

    ProccessManager(String usr) {
        user = usr;
    }

    public int compile(FileData fileData, InputStream input, OutputStream output, OutputStream error) {
        File file = new File("Users/" + user);
        file.mkdirs();
        file = new File("Users/" + user + "/" + fileData.getName());
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
        PrintStream userOut = new PrintStream(output);
        PrintStream userErr = new PrintStream(error);
        PrintStream systemOut = System.out;
        PrintStream systemErr = System.err;
        InputStream systemIn = System.in;
        System.setOut(systemOut);
        System.setErr(systemErr);
        System.setIn(systemIn);
        try {
            URLClassLoader directory = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + "\\Users\\" + user + "\\")});
            System.out.println("file://" + System.getProperty("user.dir") + "/Users/" + user + "\\" + fileData.getName().substring(0, fileData.getName().length() - ".java".length()));
            Class selection = Class.forName(fileData.getName().substring(0, fileData.getName().length() - ".java".length()), true, directory);
            Method main = selection.getDeclaredMethod("main", String[].class);
            userOut.print("Success! ");
            System.setOut(userOut);
            System.setErr(userOut); //last minute fix
            System.setIn(input);
            System.out.println("Executing program...\n");
            try {
                main.invoke(null, (Object) new String[]{"test"});
                System.out.println("\nExecution ran successfully");
            } catch (Exception e) {
                e.getCause().printStackTrace();
            }

        } catch (ClassNotFoundException | NoSuchMethodException | MalformedURLException e) {
            userErr.println("\nCompilation failed due to an unknown error");
        }
        finally {
            System.out.flush();
            System.err.flush();
            userOut.close();
            userErr.close();
            fileData.write("Users/" + user);
        }
    }
}
