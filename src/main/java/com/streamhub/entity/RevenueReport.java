package com.streamhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "revenue_report", indexes = {
    @Index(name = "idx_report_dates", columnList = "report_period_start, report_period_end")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_period_start", nullable = false)
    private LocalDate reportPeriodStart;

    @Column(name = "report_period_end", nullable = false)
    private LocalDate reportPeriodEnd;

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "active_users_count")
    private Integer activeUsersCount;

    @Column(columnDefinition = "JSON")
    private String metrics;

    @Column(name = "generated_date", nullable = false, updatable = false)
    private LocalDateTime generatedDate;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.generatedDate = LocalDateTime.now();
    }
}
