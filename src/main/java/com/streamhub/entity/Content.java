package com.streamhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Content Entity - Represents a piece of content (Movie, Music, Ebook, etc.)
 * Mapped to the 'content' table in MySQL database
 */
@Entity
@Table(name = "content", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_content_type", columnList = "content_type"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class Content {

    /**
     * Unique identifier for the content
     * Auto-generated primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Title of the content
     * Cannot be null, unique constraint
     */
    @Column(name = "title", nullable = false, length = 255, unique = true)
    private String title;

    /**
     * Description of the content
     * Allows up to 5000 characters
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Type of content: MOVIE, MUSIC, EBOOK, SERIES
     */
    @Column(name = "content_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    /**
     * URL to the content file
     * Could be local path or S3/cloud storage URL
     */
    @Column(name = "content_url", nullable = false, length = 500)
    private String contentUrl;

    /**
     * Duration in minutes (for movies/music/series episodes)
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * Genre of the content
     * Examples: Action, Comedy, Thriller, Romance, etc.
     */
    @Column(name = "genre", length = 100)
    private String genre;

    /**
     * Release date of the content
     */
    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    /**
     * IMDb/Rating score (1-10)
     */
    @Column(name = "rating")
    private Double rating;

    /**
     * Thumbnail/Poster image URL
     */
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /**
     * Language of the content
     * Examples: English, Spanish, Hindi, etc.
     */
    @Column(name = "language", length = 50)
    private String language;

    /**
     * Director name (for movies)
     */
    @Column(name = "director", length = 255)
    private String director;

    /**
     * Cast members (comma separated or JSON)
     */
    @Column(name = "cast", columnDefinition = "TEXT")
    private String cast;

    /**
     * Whether content is available (active/inactive)
     */
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    /**
     * Whether content requires premium subscription
     */
    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;

    /**
     * View count for analytics
     */
    @Column(name = "view_count")
    private Long viewCount = 0L;

    /**
     * Number of likes/favorites
     */
    @Column(name = "likes_count")
    private Long likesCount = 0L;

    /**
     * Timestamp when content was created
     * Automatically set when entity is first persisted
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when content was last updated
     * Automatically updated on every modification
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * JPA Lifecycle hook - automatically set createdAt before insert
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA Lifecycle hook - automatically update updatedAt before update
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Content() {
    }

    public Content(Long id, String title, String description, ContentType contentType, String contentUrl,
            Integer durationMinutes, String genre, LocalDateTime releaseDate, Double rating,
            String thumbnailUrl, String language, String director, String cast, Boolean isAvailable,
            Boolean isPremium, Long viewCount, Long likesCount, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.thumbnailUrl = thumbnailUrl;
        this.language = language;
        this.director = director;
        this.cast = cast;
        this.isAvailable = isAvailable;
        this.isPremium = isPremium;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", contentType=" + contentType +
                ", contentUrl='" + contentUrl + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", genre='" + genre + '\'' +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", language='" + language + '\'' +
                ", director='" + director + '\'' +
                ", cast='" + cast + '\'' +
                ", isAvailable=" + isAvailable +
                ", isPremium=" + isPremium +
                ", viewCount=" + viewCount +
                ", likesCount=" + likesCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Content content = (Content) o;
        return Objects.equals(id, content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Enum for Content Types
     */
    public enum ContentType {
        MOVIE,
        MUSIC,
        EBOOK,
        SERIES,
        PODCAST,
        DOCUMENTARY,
        STAND_UP
    }
}
