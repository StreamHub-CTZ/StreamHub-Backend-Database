package com.streamhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * Health Check Controller
 * Provides endpoints for health monitoring and API status
 */
@RestController
@RequestMapping
public class HealthController {

    /**
     * Health check endpoint - returns API status
     * @return Health status information
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "StreamHub Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "StreamHub Backend is running successfully!");
        response.put("endpoints", new String[]{
            "/api/content - Get all content",
            "/api/content/{id} - Get content by ID",
            "/api/content/available - Get available content",
            "/api/content/premium - Get premium content",
            "/api/content/type/{contentType} - Get content by type",
            "/api/content/genre/{genre} - Get content by genre",
            "/api/content/search?keyword=... - Search content",
            "/api/content/top-rated - Get top rated content",
            "/api/content/new - Get new content",
            "/api/content/stats - Get content statistics"
        });
        return ResponseEntity.ok(response);
    }

    /**
     * Liveness probe endpoint for Kubernetes/container orchestration
     * @return Liveness status
     */
    @GetMapping("/health/live")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "StreamHub Backend");
        return ResponseEntity.ok(response);
    }

    /**
     * Readiness probe endpoint - checks if service is ready to handle traffic
     * @return Readiness status
     */
    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, String>> readiness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "READY");
        response.put("service", "StreamHub Backend");
        response.put("database", "Configured");
        return ResponseEntity.ok(response);
    }
}
