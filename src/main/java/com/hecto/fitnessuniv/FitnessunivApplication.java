package com.hecto.fitnessuniv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FitnessunivApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessunivApplication.class, args);
    }
}
