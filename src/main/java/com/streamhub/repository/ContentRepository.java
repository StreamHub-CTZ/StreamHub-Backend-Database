package com.streamhub.repository;

import com.streamhub.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findByContentType(String contentType, Pageable pageable);

    Page<Content> findByStatus(String status, Pageable pageable);

    Page<Content> findByGenre(String genre, Pageable pageable);

    List<Content> findByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT c FROM Content c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Content> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    long countByStatus(String status);

    boolean existsByTitle(String title);
}
