package com.seji.kidcuiside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Calendar;

@SpringBootApplication
public class KidCuisIdeApplication {

    public static void main(String[] args) {
        try {
            File outputlog = new File("logs/" + Calendar.getInstance().getTime() + "-out.log");
            outputlog.createNewFile();
            PrintStream out = new PrintStream(outputlog);
            System.setOut(out);
            System.setErr(out);
        } catch (IOException e) {
            System.err.println("Unable to start logging, printing to terminal instead");
            e.printStackTrace();
        }
        SpringApplication.run(KidCuisIdeApplication.class, args);
    }

}