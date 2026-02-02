package com.streamhub.repository;

import com.streamhub.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Page<Subscription> findByUserId(Long userId, Pageable pageable);
    Page<Subscription> findByStatus(String status, Pageable pageable);
    List<Subscription> findByEndDateBefore(LocalDate date);
    List<Subscription> findByUserIdAndStatus(Long userId, String status);
}
