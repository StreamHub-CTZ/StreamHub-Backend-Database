package com.streamhub.repository;

import com.streamhub.entity.SystemAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemAuditLogRepository extends JpaRepository<SystemAuditLog, Long> {
    Page<SystemAuditLog> findByTableName(String tableName, Pageable pageable);
    Page<SystemAuditLog> findByAction(String action, Pageable pageable);
    List<SystemAuditLog> findByTableNameAndRecordId(String tableName, Long recordId);
    List<SystemAuditLog> findByPerformedByAndTimestampBetween(String performedBy, LocalDateTime start, LocalDateTime end);
    Page<SystemAuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
