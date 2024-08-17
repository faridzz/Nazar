package org.example.nazar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NazarApplication {

    public static void main(String[] args) {
        SpringApplication.run(NazarApplication.class, args);
    }

}
