package com.streamhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_control_log", indexes = {
    @Index(name = "idx_access_content", columnList = "content_id"),
    @Index(name = "idx_access_user_time", columnList = "user_id, timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessControlLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "access_status", nullable = false, length = 50)
    private String accessStatus;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "content_title_snapshot", length = 255)
    private String contentTitleSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude
    private AppUser appUser;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
