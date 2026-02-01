package com.streamhub.repository;

import com.streamhub.entity.Content;
import com.streamhub.entity.Content.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ContentRepository - JPA Repository for Content Entity
 * Provides database operations for Content table
 * Spring Data JPA automatically implements CRUD operations
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    /**
     * Find content by title
     * @param title The title to search for
     * @return Optional containing the content if found
     */
    Optional<Content> findByTitle(String title);

    /**
     * Find all content of a specific type
     * @param contentType The type of content (MOVIE, MUSIC, etc.)
     * @return List of all content matching the type
     */
    List<Content> findByContentType(ContentType contentType);

    /**
     * Find all available content
     * @return List of all available/active content
     */
    List<Content> findByIsAvailableTrue();

    /**
     * Find premium content only
     * @return List of all premium content
     */
    List<Content> findByIsPremiumTrue();

    /**
     * Find content by genre
     * @param genre The genre to search for
     * @return List of content matching the genre
     */
    List<Content> findByGenre(String genre);

    /**
     * Find all content created after a specific date
     * Useful for fetching newly added content
     * @param date The date to filter from
     * @return List of content created after the specified date
     */
    List<Content> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Custom query - Find top rated content
     * @param isAvailable Whether content should be available
     * @param limit Maximum number of results
     * @return List of highest rated content
     */
    @Query(value = "SELECT * FROM content WHERE is_available = :isAvailable ORDER BY rating DESC LIMIT ?2", 
           nativeQuery = true)
    List<Content> findTopRatedContent(@Param("isAvailable") Boolean isAvailable, @Param("limit") int limit);

    /**
     * Custom query - Search content by title (case-insensitive)
     * @param keyword The search keyword
     * @return List of content matching the keyword
     */
    @Query("SELECT c FROM Content c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Content> searchByTitle(@Param("keyword") String keyword);

    /**
     * Custom query - Find content by multiple criteria
     * @param contentType The type of content
     * @param isAvailable Whether content is available
     * @param minRating Minimum rating threshold
     * @return List of content matching all criteria
     */
    @Query("SELECT c FROM Content c WHERE c.contentType = :contentType " +
           "AND c.isAvailable = :isAvailable AND c.rating >= :minRating " +
           "ORDER BY c.rating DESC")
    List<Content> findByMultipleCriteria(
        @Param("contentType") ContentType contentType,
        @Param("isAvailable") Boolean isAvailable,
        @Param("minRating") Double minRating
    );

    /**
     * Get total count of available content
     * @return Total number of available content items
     */
    long countByIsAvailableTrue();

    /**
     * Get total count of premium content
     * @return Total number of premium content items
     */
    long countByIsPremiumTrue();

    /**
     * Check if content exists by title
     * @param title The title to check
     * @return true if content exists, false otherwise
     */
    boolean existsByTitle(String title);
}
