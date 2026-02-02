package com.streamhub.controller;

import com.streamhub.dto.CatalogResponseDTO;
import com.streamhub.entity.Content;
import com.streamhub.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
public class ContentController {

    private static final Logger logger = Logger.getLogger(ContentController.class.getName());
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/catalog")
    public ResponseEntity<CatalogResponseDTO> getCatalog(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection) {
        
        logger.info("GET request: Fetch catalog - page=" + page + ", pageSize=" + pageSize + 
                    ", sortBy=" + sortBy + ", sortDirection=" + sortDirection);
        
        try {
            CatalogResponseDTO response = contentService.getCatalogResponse(page, pageSize, sortBy, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching catalog: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<Content> getContentById(@PathVariable Long contentId) {
        logger.info("GET request: Fetch content with ID: " + contentId);
        return contentService.getContentById(contentId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /v1/content/type/{contentType} - Get content by type with pagination and sorting
     */
    @GetMapping("/content/type/{contentType}")
    public ResponseEntity<CatalogResponseDTO> getContentByType(
            @PathVariable String contentType,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection) {
        
        logger.info("GET request: Fetch content by type - " + contentType + ", page=" + page);
        
        try {
            CatalogResponseDTO response = contentService.getContentByTypeResponse(contentType, page, pageSize, sortBy, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching content by type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /v1/content/genre/{genre} - Get content by genre with pagination and sorting
     */
    @GetMapping("/content/genre/{genre}")
    public ResponseEntity<CatalogResponseDTO> getContentByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection) {
        
        logger.info("GET request: Fetch content by genre - " + genre + ", page=" + page);
        
        try {
            CatalogResponseDTO response = contentService.getContentByGenreResponse(genre, page, pageSize, sortBy, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching content by genre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /v1/search - Search content by keyword with pagination and sorting
     */
    @GetMapping("/search")
    public ResponseEntity<CatalogResponseDTO> searchContent(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection) {
        
        logger.info("GET request: Search content with keyword - " + keyword + ", page=" + page);
        
        try {
            CatalogResponseDTO response = contentService.searchContentResponse(keyword, page, pageSize, sortBy, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error searching content: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ContentService.ContentStats> getStatistics() {
        logger.info("GET request: Fetch content statistics");
        ContentService.ContentStats stats = contentService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/content")
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

    @PutMapping("/content/{contentId}")
    public ResponseEntity<Content> updateContent(
        @PathVariable Long contentId,
        @Valid @RequestBody Content content) {
        logger.info("PUT request: Update content with ID: " + contentId);
        try {
            Content updatedContent = contentService.updateContent(contentId, content);
            return ResponseEntity.ok(updatedContent);
        } catch (RuntimeException e) {
            logger.severe("Error updating content: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Map<String, String>> deleteContent(@PathVariable Long contentId) {
        logger.info("DELETE request: Delete content with ID: " + contentId);
        try {
            contentService.deleteContent(contentId);
            return ResponseEntity.ok(Map.of("message", "Content deleted successfully"));
        } catch (RuntimeException e) {
            logger.severe("Error deleting content: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        logger.severe("Invalid argument: " + e.getMessage());
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
    }
}
