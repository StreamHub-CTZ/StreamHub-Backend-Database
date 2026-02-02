package com.streamhub.repository;

import com.streamhub.entity.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Page<PaymentTransaction> findByUserId(Long userId, Pageable pageable);
    Page<PaymentTransaction> findByTransactionStatus(String status, Pageable pageable);
    List<PaymentTransaction> findBySubscriptionId(Long subscriptionId);
    List<PaymentTransaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
