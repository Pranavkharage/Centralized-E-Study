package com.pranav.estudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EStudyApplication.class, args);
        System.out.println("Centralized E-Study Platform running at http://localhost:8080");
    }
}
