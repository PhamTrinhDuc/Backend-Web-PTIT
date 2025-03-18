package com.javaweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// add dependencies to pom.xml => mvn spring-boot:run
// hot reload or live reload => File > Settings => Build, Execution, Deployment > Compiler => "Build project automatically"
@SpringBootApplication
public class EcommereWeb {
    public static void main(String[] args) {
        SpringApplication.run(EcommereWeb.class, args);
    }
}