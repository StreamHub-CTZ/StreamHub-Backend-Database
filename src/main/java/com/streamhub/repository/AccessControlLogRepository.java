package com.streamhub.repository;

import com.streamhub.entity.AccessControlLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessControlLogRepository extends JpaRepository<AccessControlLog, Long> {
    Page<AccessControlLog> findByContentId(Long contentId, Pageable pageable);
    Page<AccessControlLog> findByUserId(Long userId, Pageable pageable);
    Page<AccessControlLog> findByAccessStatus(String status, Pageable pageable);
    List<AccessControlLog> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<AccessControlLog> findByContentIdAndAccessStatus(Long contentId, String status);
}
