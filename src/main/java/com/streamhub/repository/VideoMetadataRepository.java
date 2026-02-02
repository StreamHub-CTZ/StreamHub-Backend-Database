package com.streamhub.repository;

import com.streamhub.entity.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long> {
    Optional<VideoMetadata> findByContentId(Long contentId);
    boolean existsByContentId(Long contentId);
}
