package com.streamhub.controller;

import com.streamhub.entity.Content;
import com.streamhub.entity.Content.ContentType;
import com.streamhub.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * ContentController - REST API endpoints for Content management
 * Base URL: /api/content
 * Handles HTTP requests for content operations
 */
@RestController
@RequestMapping("/content")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContentController {

    private static final Logger logger = Logger.getLogger(ContentController.class.getName());
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * GET /api/content - Get all content
     * @return List of all content items
     */
    @GetMapping
    public ResponseEntity<List<Content>> getAllContent() {
        logger.info("GET request: Fetch all content");
        List<Content> content = contentService.getAllContent();
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/{id} - Get content by ID
     * @param id The content ID
     * @return Content details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        logger.info("GET request: Fetch content with ID: " + id);
        return contentService.getContentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/content/available - Get only available content
     * @return List of available content
     */
    @GetMapping("/available")
    public ResponseEntity<List<Content>> getAvailableContent() {
        logger.info("GET request: Fetch available content");
        List<Content> content = contentService.getAvailableContent();
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/premium - Get premium content
     * @return List of premium content
     */
    @GetMapping("/premium")
    public ResponseEntity<List<Content>> getPremiumContent() {
        logger.info("GET request: Fetch premium content");
        List<Content> content = contentService.getPremiumContent();
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/type/{contentType} - Get content by type
     * @param contentType The type (MOVIE, MUSIC, EBOOK, etc.)
     * @return List of content matching the type
     */
    @GetMapping("/type/{contentType}")
    public ResponseEntity<List<Content>> getContentByType(@PathVariable ContentType contentType) {
        logger.info("GET request: Fetch content by type: " + contentType);
        List<Content> content = contentService.getContentByType(contentType);
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/genre/{genre} - Get content by genre
     * @param genre The genre to filter by
     * @return List of content matching the genre
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Content>> getContentByGenre(@PathVariable String genre) {
        logger.info("GET request: Fetch content by genre: " + genre);
        List<Content> content = contentService.getContentByGenre(genre);
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/search - Search content by keyword
     * @param keyword The search keyword
     * @return List of content matching the keyword
     */
    @GetMapping("/search")
    public ResponseEntity<List<Content>> searchContent(@RequestParam String keyword) {
        logger.info("GET request: Search content with keyword: " + keyword);
        List<Content> content = contentService.searchContent(keyword);
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/top-rated - Get top rated content
     * @param limit Number of results (default: 10)
     * @return List of top rated content
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<Content>> getTopRatedContent(@RequestParam(defaultValue = "10") int limit) {
        logger.info("GET request: Fetch top " + limit + " rated content");
        List<Content> content = contentService.getTopRatedContent(limit);
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/new - Get newly added content
     * @param days Number of days to look back (default: 7)
     * @return List of newly added content
     */
    @GetMapping("/new")
    public ResponseEntity<List<Content>> getNewContent(@RequestParam(defaultValue = "7") int days) {
        logger.info("GET request: Fetch content added in last " + days + " days");
        List<Content> content = contentService.getNewContent(days);
        return ResponseEntity.ok(content);
    }

    /**
     * GET /api/content/stats - Get content statistics
     * @return Statistics about content
     */
    @GetMapping("/stats")
    public ResponseEntity<ContentService.ContentStats> getStatistics() {
        logger.info("GET request: Fetch content statistics");
        ContentService.ContentStats stats = contentService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * POST /api/content - Create new content
     * @param content The content object to create
     * @return Created content with ID
     */
    @PostMapping
    public ResponseEntity<Content> createContent(@Valid @RequestBody Content content) {
        logger.info("POST request: Create new content - " + content.getTitle());
        try {
            Content createdContent = contentService.createContent(content);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContent);
        } catch (IllegalArgumentException e) {
            logger.severe("Error creating content: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/content/{id} - Update existing content
     * @param id The content ID
     * @param content The updated content object
     * @return Updated content
     */
    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(
        @PathVariable Long id,
        @Valid @RequestBody Content content) {
        logger.info("PUT request: Update content with ID: " + id);
        try {
            Content updatedContent = contentService.updateContent(id, content);
            return ResponseEntity.ok(updatedContent);
        } catch (RuntimeException e) {
            logger.severe("Error updating content: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/content/{id} - Delete content
     * @param id The content ID
     * @return Success or failure response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteContent(@PathVariable Long id) {
        logger.info("DELETE request: Delete content with ID: " + id);
        try {
            contentService.deleteContent(id);
            return ResponseEntity.ok(Map.of("message", "Content deleted successfully"));
        } catch (RuntimeException e) {
            logger.severe("Error deleting content: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/content/{id}/view - Increment view count
     * @param id The content ID
     * @return Success response
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Map<String, String>> incrementViewCount(@PathVariable Long id) {
        logger.info("POST request: Increment view count for content ID: " + id);
        contentService.incrementViewCount(id);
        return ResponseEntity.ok(Map.of("message", "View count incremented"));
    }

    /**
     * Error handler for invalid input
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        logger.severe("Invalid argument: " + e.getMessage());
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
    }
}
