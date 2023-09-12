package com.culflab.jwtauthsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JwtAuthSbApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthSbApplication.class, args);
    }

}
