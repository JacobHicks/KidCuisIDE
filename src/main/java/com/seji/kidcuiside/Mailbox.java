package com.seji.kidcuiside;

import com.seji.kidcuiside.forms.ConsoleOutput;
import com.seji.kidcuiside.forms.FullRunRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mailbox {
    private static ConcurrentHashMap<String, Object[]> outputQueue;
    private static ConcurrentLinkedQueue<FullRunRequest> runRequests;

    private static CodeRunner runtime;

    public static void init() {
        outputQueue = new ConcurrentHashMap<>();
        runRequests = new ConcurrentLinkedQueue<>();
        runtime = new CodeRunner();
        Thread runThread = new Thread(runtime);
        runThread.start();
    }

    public static void start(String sessionId) {
        outputQueue.put(sessionId, new Object[4]);
        outputQueue.get(sessionId)[3] = false;
    }

    public static void stop(String sessionId) {
        runtime.stopCode();
        outputQueue.get(sessionId)[3] = true;
    }

    public static void addOutputTrail(String sessionId, InputStream out, InputStream err) {
        outputQueue.get(sessionId)[0] = out;
        outputQueue.get(sessionId)[1] = err;
    }

    public static void addInputTrail(String sessionId, OutputStream in) {
        outputQueue.get(sessionId)[2] = in;
    }

    public static void sendMail(String sessionId, String message) {
        while (!outputQueue.containsKey(sessionId) || outputQueue.get(sessionId)[2] == null) ; //TODO Make this time out
        try {
            ((OutputStream) outputQueue.get(sessionId)[2]).write(message.getBytes());
            ((OutputStream) outputQueue.get(sessionId)[2]).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConsoleOutput getMail(String sessionId) {
        ConsoleOutput res = new ConsoleOutput();
        res.setEof(false);
        try {
            while(outputQueue.get(sessionId)[0] == null);
            if (outputQueue.get(sessionId)[3].equals(true) && ((InputStream) outputQueue.get(sessionId)[0]).available() == 0 && ((InputStream) outputQueue.get(sessionId)[1]).available() == 0) {
                res.setEof(true);
                return res;
            } else {
                while (((!outputQueue.containsKey(sessionId) || outputQueue.get(sessionId)[0] == null || ((InputStream) outputQueue.get(sessionId)[0]).available() == 0)) && (((InputStream) outputQueue.get(sessionId)[1]) != null || ((InputStream) outputQueue.get(sessionId)[1]).available() <= 0)) {
                    if (outputQueue.get(sessionId)[3].equals(true)) {
                        res.setEof(true);
                        return res;
                    }
                } //TODO Make this time out
                if (outputQueue.containsKey(sessionId)) {
                    if (((InputStream) outputQueue.get(sessionId)[0]) != null && ((InputStream) outputQueue.get(sessionId)[0]).available() > 0) {
                        byte[] messagebytes = new byte[((InputStream) outputQueue.get(sessionId)[0]).available()];
                        ((InputStream) outputQueue.get(sessionId)[0]).read(messagebytes);
                        res.setOutput(new String(messagebytes));
                    } else {
                        res.setOutput("");
                    }
                    if (((InputStream) outputQueue.get(sessionId)[1]) != null && ((InputStream) outputQueue.get(sessionId)[1]).available() > 0) {
                        byte[] messagebytes = new byte[((InputStream) outputQueue.get(sessionId)[1]).available()];
                        ((InputStream) outputQueue.get(sessionId)[1]).read(messagebytes);
                        res.setError(new String(messagebytes));
                    } else {
                        res.setError("");
                    }
                } else {
                    res.setEof(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void addRequest(FullRunRequest runRequest) {
        runRequests.offer(runRequest);
    }

    public static FullRunRequest next() {
        return runRequests.poll();
    }

    public static void deleteAll() {
        if (runtime != null) {
            runtime.stop();
        }
        outputQueue.clear();
        runRequests.clear();
        if (runtime != null) {
            runtime.start();
        }
    }
}