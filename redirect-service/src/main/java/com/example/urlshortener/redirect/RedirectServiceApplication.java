package com.example.urlshortener.redirect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RedirectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedirectServiceApplication.class, args);
    }
}
