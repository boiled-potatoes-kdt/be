package com.dain_review;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DainReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(DainReviewApplication.class, args);
    }
}
