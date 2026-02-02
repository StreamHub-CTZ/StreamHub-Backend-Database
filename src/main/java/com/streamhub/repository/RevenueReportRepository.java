package com.streamhub.repository;

import com.streamhub.entity.RevenueReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueReportRepository extends JpaRepository<RevenueReport, Long> {
    List<RevenueReport> findByReportPeriodStartAndReportPeriodEnd(LocalDate start, LocalDate end);
    Page<RevenueReport> findByReportPeriodStartGreaterThanEqual(LocalDate start, Pageable pageable);
    List<RevenueReport> findByOrderByReportPeriodStartDesc();
}
