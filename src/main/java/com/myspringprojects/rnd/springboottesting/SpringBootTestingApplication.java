package com.myspringprojects.rnd.springboottesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.myspringprojects.rnd.springboottesting")
public class SpringBootTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestingApplication.class, args);
    }

}
