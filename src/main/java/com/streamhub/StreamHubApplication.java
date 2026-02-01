package com.streamhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * StreamHub Backend Application
 * Main entry point for the Spring Boot application
 * 
 * Features:
 * - REST API for content management
 * - MySQL database integration with JPA/Hibernate
 * - CORS configuration for frontend communication
 * - Comprehensive logging and error handling
 */
@SpringBootApplication
public class StreamHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamHubApplication.class, args);
        System.out.println("=====================================");
        System.out.println("StreamHub Backend Started Successfully");
        System.out.println("API Base URL: http://localhost:8090/api");
        System.out.println("=====================================");
    }

    /**
     * CORS Configuration
     * Allows frontend to make requests to this backend
     */
    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",  // Vite dev server
                    "http://localhost:3000",  // Alternative React dev server
                    "http://localhost:8080",  // Local testing
                    "https://yourdomain.com"  // Production domain
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        }
    }
}
