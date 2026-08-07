package com.spms;

import com.spms.config.DotEnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpmsApplication {

    public static void main(String[] args) {
        // Load secrets from local .env before Spring starts
        // (so IDE Run works without manual env var setup)
        DotEnvLoader.load();

        SpringApplication.run(SpmsApplication.class, args);
    }

}
