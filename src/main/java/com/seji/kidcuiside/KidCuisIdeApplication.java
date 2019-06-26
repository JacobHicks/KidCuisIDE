package com.seji.kidcuiside;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.util.Calendar;

@SpringBootApplication
public class KidCuisIdeApplication {

    public static void main(String[] args) {
        Mailbox.init();
        SpringApplication.run(KidCuisIdeApplication.class, args);
    }
}