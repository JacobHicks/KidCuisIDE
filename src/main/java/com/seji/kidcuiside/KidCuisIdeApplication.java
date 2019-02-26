package com.seji.kidcuiside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;

@SpringBootApplication
public class KidCuisIdeApplication {

    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream(new File(Calendar.getInstance().toString() + "-out.log"));
            PrintStream err = new PrintStream(new File(Calendar.getInstance().toString() + "-err.log"));
            System.setOut(out);
            System.setErr(err);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to start logging, printing to terminal instead");
            e.printStackTrace();
        }
        SpringApplication.run(KidCuisIdeApplication.class, args);
    }

}

