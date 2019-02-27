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
            File errlog = new File("logs/" + Calendar.getInstance().getTime() + "-err.log");
            outputlog.createNewFile();
            errlog.createNewFile();
            PrintStream out = new PrintStream(outputlog);
            PrintStream err = new PrintStream(errlog);
            System.setOut(out);
            System.setErr(err);
        } catch (IOException e) {
            System.err.println("Unable to start logging, printing to terminal instead");
            e.printStackTrace();
        }
        SpringApplication.run(KidCuisIdeApplication.class, args);
    }

}

