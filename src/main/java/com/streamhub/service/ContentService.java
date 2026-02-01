package com.streamhub.service;

import com.streamhub.entity.Content;
import com.streamhub.entity.Content.ContentType;
import com.streamhub.repository.ContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * ContentService - Business logic layer for Content
 * Handles all operations related to content management
 * Provides abstraction between controller and repository
 */
@Service
@Transactional
public class ContentService {

    private static final Logger logger = Logger.getLogger(ContentService.class.getName());
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * Create a new content item
     * @param content The content object to save
     * @return Saved content with generated ID
     */
    public Content createContent(Content content) {
        logger.info("Creating new content: " + content.getTitle());
        
        // Validate title is unique
        if (contentRepository.existsByTitle(content.getTitle())) {
            logger.warning("Content with title '" + content.getTitle() + "' already exists");
            throw new IllegalArgumentException("Content with this title already exists");
        }
        
        Content savedContent = contentRepository.save(content);
        logger.info("Content created successfully with ID: " + savedContent.getId());
        return savedContent;
    }

    /**
     * Get content by ID
     * @param id The content ID
     * @return Optional containing the content if found
     */
    public Optional<Content> getContentById(Long id) {
        logger.fine("Fetching content with ID: " + id);
        return contentRepository.findById(id);
    }

    /**
     * Get all content
     * @return List of all content
     */
    public List<Content> getAllContent() {
        logger.info("Fetching all content");
        return contentRepository.findAll();
    }

    /**
     * Get all available content
     * @return List of available content
     */
    public List<Content> getAvailableContent() {
        logger.info("Fetching all available content");
        return contentRepository.findByIsAvailableTrue();
    }

    /**
     * Get content by type
     * @param contentType The type of content
     * @return List of content matching the type
     */
    public List<Content> getContentByType(ContentType contentType) {
        logger.info("Fetching content of type: " + contentType);
        return contentRepository.findByContentType(contentType);
    }

    /**
     * Get premium content
     * @return List of all premium content
     */
    public List<Content> getPremiumContent() {
        logger.info("Fetching premium content");
        return contentRepository.findByIsPremiumTrue();
    }

    /**
     * Get content by genre
     * @param genre The genre to search for
     * @return List of content matching the genre
     */
    public List<Content> getContentByGenre(String genre) {
        logger.info("Fetching content with genre: " + genre);
        return contentRepository.findByGenre(genre);
    }

    /**
     * Search content by title (case-insensitive)
     * @param keyword The search keyword
     * @return List of content matching the keyword
     */
    public List<Content> searchContent(String keyword) {
        logger.info("Searching content with keyword: " + keyword);
        return contentRepository.searchByTitle(keyword);
    }

    /**
     * Get top rated content
     * @param limit Number of results to return
     * @return List of top rated content
     */
    public List<Content> getTopRatedContent(int limit) {
        logger.info("Fetching top " + limit + " rated content");
        return contentRepository.findTopRatedContent(true, limit);
    }

    /**
     * Get newly added content (last N days)
     * @param days Number of days to look back
     * @return List of content added in the specified period
     */
    public List<Content> getNewContent(int days) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(days);
        logger.info("Fetching content added in last " + days + " days");
        return contentRepository.findByCreatedAtAfter(dateThreshold);
    }

    /**
     * Update content
     * @param id The content ID
     * @param updatedContent The updated content details
     * @return Updated content
     */
    public Content updateContent(Long id, Content updatedContent) {
        logger.info("Updating content with ID: " + id);
        
        return contentRepository.findById(id)
            .map(existing -> {
                existing.setTitle(updatedContent.getTitle());
                existing.setDescription(updatedContent.getDescription());
                existing.setContentType(updatedContent.getContentType());
                existing.setContentUrl(updatedContent.getContentUrl());
                existing.setDurationMinutes(updatedContent.getDurationMinutes());
                existing.setGenre(updatedContent.getGenre());
                existing.setReleaseDate(updatedContent.getReleaseDate());
                existing.setRating(updatedContent.getRating());
                existing.setThumbnailUrl(updatedContent.getThumbnailUrl());
                existing.setLanguage(updatedContent.getLanguage());
                existing.setDirector(updatedContent.getDirector());
                existing.setCast(updatedContent.getCast());
                
                Content saved = contentRepository.save(existing);
                logger.info("Content with ID " + id + " updated successfully");
                return saved;
            })
            .orElseThrow(() -> {
                logger.severe("Content with ID " + id + " not found");
                return new RuntimeException("Content not found with ID: " + id);
            });
    }

    /**
     * Delete content by ID
     * @param id The content ID
     */
    public void deleteContent(Long id) {
        logger.info("Deleting content with ID: " + id);
        
        if (!contentRepository.existsById(id)) {
            logger.severe("Content with ID " + id + " not found");
            throw new RuntimeException("Content not found with ID: " + id);
        }
        
        contentRepository.deleteById(id);
        logger.info("Content with ID " + id + " deleted successfully");
    }

    /**
     * Increment view count for a content
     * @param id The content ID
     */
    @Transactional
    public void incrementViewCount(Long id) {
        contentRepository.findById(id).ifPresent(content -> {
            contentRepository.save(content);
            logger.fine("View count incremented for content ID: " + id);
        });
    }

    /**
     * Get statistics about content
     * @return Statistics including total count, premium count, etc.
     */
    public ContentStats getStatistics() {
        logger.info("Fetching content statistics");
        return new ContentStats(
            contentRepository.count(),
            contentRepository.countByIsAvailableTrue(),
            contentRepository.countByIsPremiumTrue()
        );
    }

    /**
     * Inner class for content statistics
     */
    public static class ContentStats {
        private Long totalContent;
        private Long availableContent;
        private Long premiumContent;

        public ContentStats(Long totalContent, Long availableContent, Long premiumContent) {
            this.totalContent = totalContent;
            this.availableContent = availableContent;
            this.premiumContent = premiumContent;
        }

        public Long getTotalContent() {
            return totalContent;
        }

        public void setTotalContent(Long totalContent) {
            this.totalContent = totalContent;
        }

        public Long getAvailableContent() {
            return availableContent;
        }

        public void setAvailableContent(Long availableContent) {
            this.availableContent = availableContent;
        }

        public Long getPremiumContent() {
            return premiumContent;
        }

        public void setPremiumContent(Long premiumContent) {
            this.premiumContent = premiumContent;
        }

        @Override
        public String toString() {
            return "ContentStats{" +
                    "totalContent=" + totalContent +
                    ", availableContent=" + availableContent +
                    ", premiumContent=" + premiumContent +
                    '}';
        }
    }
}
