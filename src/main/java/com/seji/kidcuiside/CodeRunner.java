package com.seji.kidcuiside;

import com.seji.kidcuiside.forms.FullRunRequest;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

class CodeRunner implements Runnable {
    private final ProcessBuilder processBuilder;
    private final AtomicBoolean enabled;
    private final AtomicBoolean running;

    private final String[] types = new String[]{"java"};
    final Mailbox dbg = new Mailbox();

    CodeRunner() {
        enabled = new AtomicBoolean(true);
        running = new AtomicBoolean(false);
        processBuilder = new ProcessBuilder();
    }

    @Override
    public void run() {
        enabled.set(true);
        while (enabled.get()) {
            running.set(true);
            FullRunRequest runRequest = Mailbox.next();
            if (runRequest != null) {
                Mailbox.start(runRequest.getSessionId());
                if (Pattern.matches("\\W.*", runRequest.getName())) {
                    //TODO we need a message for stuff like this, ez but its 3am
                } else {
                    boolean isValidLanguage = false;
                    for (String s : types) {
                        if (s.equals(runRequest.getLanguage())) {
                            isValidLanguage = true;
                            break;
                        }
                    }
                    if (runRequest.getCode().length() <= 1024000) {
                        if (isValidLanguage) {
                            File file = saveCode(runRequest);
                            if (file != null) {
                                try {
                                    switch (runRequest.getLanguage()) {
                                        case ("java"):
                                            processBuilder.command("javac", file.getAbsolutePath());
                                            break;
                                    }
                                    Process process = processBuilder.start();
                                    Mailbox.addOutputTrail(runRequest.getSessionId(), process.getInputStream(), process.getErrorStream());
                                    while (process.isAlive()) ;

                                    int endPathIndex = file.getAbsolutePath().lastIndexOf("\\" + runRequest.getName());
                                    if (endPathIndex == -1)
                                        endPathIndex = file.getAbsolutePath().lastIndexOf("/" + runRequest.getName());
                                    switch (runRequest.getLanguage()) {
                                        case ("java"): //+ or minus memory based on languages too
                                            processBuilder.command("java", "-cp", file.getAbsolutePath().substring(0, endPathIndex), "-Djava.security.manager", "-Djava.security.policy=javacodeexecutionpolicy", "-Xmx512M", runRequest.getName());
                                            break;
                                    }

                                    process = processBuilder.start();
                                    Mailbox.addOutputTrail(runRequest.getSessionId(), process.getInputStream(), process.getErrorStream());
                                    Mailbox.addInputTrail(runRequest.getSessionId(), process.getOutputStream());

                                    long startTime = System.currentTimeMillis();
                                    long timeElapsed = 0;
                                    boolean withinTimeLimits = true;  //TODO: tell users there out of time
                                    while (process.isAlive()) {
                                        if (timeElapsed / 1000.0 >= 600) {
                                            withinTimeLimits = false;
                                            process.destroyForcibly();
                                        } else {
                                            timeElapsed = Math.max((System.currentTimeMillis() - startTime), 0);
                                        }
                                    }

                                } catch (IOException io) {
                                    //TODO yup
                                }
                            }
                        }
                    }
                }
                Mailbox.stop(runRequest.getSessionId());
            }
        }
        running.set(false);
    }

    private File saveCode(FullRunRequest runRequest) {
        try {
            File file = new File("Users/" + runRequest.getPath() + runRequest.getName() + "." + runRequest.getLanguage());
            if (file.exists()) file.delete();
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

    void stop() {
        while (running.get()) ;
        enabled.set(false);
    }

    void start() {
        enabled.set(true);
    }
}