package com.onemoreuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the One More Use Spring Boot application.
 *
 * This class boots up the embedded Tomcat web server, initializes the
 * Spring application context, configures Hibernate/JPA, and serves the REST APIs.
 */
@SpringBootApplication
public class OneMoreUseApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneMoreUseApplication.class, args);
        System.out.println("==================================================");
        System.out.println(" One More Use Backend API is running on http://localhost:8080");
        System.out.println("==================================================");
    }
}
