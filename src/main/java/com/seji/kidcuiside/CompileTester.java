package com.seji.kidcuiside;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompileTester {
    public static void main(String[] args) {
        FileData fd = new FileData(0, "Main.java",
                "import java.util.Scanner;\n" +
                        "\n" +
                        "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Scanner in = new Scanner(System.in);\n" +
                        "        System.out.print(\"Enter Input: \"); System.out.println(\"\\n\" + in.nextLine());\n" +
                        "    }\n" +
                        " }");  //TODO read from code window
        ProccessManager pm = new ProccessManager("");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(pm.compile(fd, System.in, System.out, System.err) == 0) { //TODO set params to the code window
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
                    pm.run(fd, System.in, baos, System.err);
                    finished.set(true);
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
