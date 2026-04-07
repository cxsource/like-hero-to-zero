package com.likeherotozero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse der Spring-Boot-Anwendung "Like Hero To Zero".
 *
 * Diese Klasse dient als Einstiegspunkt der Anwendung. Die Annotation
 * {@code @SpringBootApplication} kombiniert {@code @Configuration},
 * {@code @EnableAutoConfiguration} und {@code @ComponentScan} und
 * aktiviert damit die automatische Konfiguration von Spring Boot.
 */
@SpringBootApplication
public class LikeHeroToZeroApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikeHeroToZeroApplication.class, args);
    }
}
