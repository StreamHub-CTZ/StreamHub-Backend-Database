package com.streamhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "content", indexes = {
    @Index(name = "idx_content_type", columnList = "content_type"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Column(length = 100)
    private String genre;

    @Column(length = 50)
    private String language;

    @Column(columnDefinition = "JSON")
    private String metadata;

    @Column(length = 50)
    private String rating;  // PG-13, R, etc.

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailURL;

    @Column(name = "duration")
    private Integer duration;  // Duration in minutes

    @Column(columnDefinition = "ENUM('ACTIVE', 'ARCHIVED', 'DRAFT') DEFAULT 'ACTIVE'")
    private String status;

    @Column(name = "version_id")
    @Builder.Default
    private Integer versionId = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @OneToOne(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private VideoMetadata videoMetadata;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = this.status != null ? this.status : "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
